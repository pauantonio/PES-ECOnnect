package com.econnect.API;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.API.Exceptions.InvalidTokenApiException;
import com.econnect.API.HttpClient.HttpClient;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public abstract class Service {
    // Reference to the HttpClient used to communicate with the API
    private static HttpClient _httpClient;
    // Store the secret token and insert it into the request headers
    private static String _adminToken = null;
    // Gson object used to serialize and deserialize JSON
    private final Gson gson = new Gson();
    // Called when ERROR_INVALID_TOKEN is received
    private static Runnable _tokenInvalidCallback;
    // Called when a new medal is unlocked
    private static IMedalUnlockedCallback _medalUnlockedCallback;
    // Set by the subclass to indicate whether the request needs an adminToken
    protected boolean needsToken = true;

    public static void injectHttpClient(HttpClient client) {
        _httpClient = client;
    }

    // Instantiate only from ServiceFactory, after setting the HttpClient
    protected Service() {
        if (_httpClient == null) {
            throw new RuntimeException("HttpClient not set");
        }
    }

    // Update the User token, called from LoginService
    protected static void setToken(String token) {
        if (token == null) throw new IllegalArgumentException("Token cannot be null");
        if (_adminToken != null) throw new IllegalStateException("Token already set");
        _adminToken = token;
    }
    // Invalidate the User token, called from LogoutService
    protected static void deleteToken() {
        if (_adminToken == null) throw new IllegalStateException("Session token was already deleted");
        _adminToken = null;
    }

    // Generic GET request
    protected JsonResult get(String path, Map<String,String> params) throws ApiException {
        String url = ApiConstants.BASE_URL + path;
        params = addTokenToRequest(params);
        return parseResult(getRaw(url, params));
    }
    protected String getRaw(String url, Map<String,String> params) throws ApiException {
        String result;
        try {
            result = _httpClient.get(url, params);
        }
        catch (IOException e) {
            throw new RuntimeException("Error executing GET on " + url + ": " + e.getMessage());
        }
        return result;
    }

    // Generic POST request
    protected JsonResult post(String path, Map<String,String> params, Object content) throws ApiException {
        String url = ApiConstants.BASE_URL + path;
        params = addTokenToRequest(params);
        return parseResult(postRaw(url, params, content));
    }
    protected String postRaw(String url, Map<String,String> params, Object content) throws ApiException {
        String result = null;
        try {
            result = _httpClient.post(url, params, gson.toJson(content));
        }
        catch (IOException e) {
            throw new RuntimeException("Error executing POST on " + url + ": " + e.getMessage());
        }
        return result;
    }
    
    // Generic PUT request
    protected JsonResult put(String path, Map<String,String> params, Object content) throws ApiException {
        String url = ApiConstants.BASE_URL + path;
        params = addTokenToRequest(params);
        return parseResult(putRaw(url, params, content));
    }
    protected String putRaw(String url, Map<String,String> params, Object content) throws ApiException {
        String result = null;
        try {
            result = _httpClient.put(url, params, gson.toJson(content));
        }
        catch (IOException e) {
            throw new RuntimeException("Error executing PUT on " + url + ": " + e.getMessage());
        }
        return result;
    }

    // Generic DELETE request
    protected JsonResult delete(String path, Map<String,String> params) throws ApiException {
        String url = ApiConstants.BASE_URL + path;
        params = addTokenToRequest(params);
        return parseResult(deleteRaw(url, params));
    }
    protected String deleteRaw(String url, Map<String,String> params) throws ApiException {
        String result = null;
        try {
            result = _httpClient.delete(url, params);
        }
        catch (IOException e) {
            throw new RuntimeException("Error executing DELETE on " + url + ": " + e.getMessage());
        }
        return result;
    }


    private Map<String,String> addTokenToRequest(Map<String,String> params) {
        if (!needsToken) return params;
        if (_adminToken == null) {
            throw new IllegalStateException("User token not set");
        }
        if (params == null) {
            params = new TreeMap<>();
        }
        params.put(ApiConstants.TOKEN, _adminToken);
        return params;
    }

    private JsonResult parseResult(String result) throws ApiException {
        JsonResult json = JsonResult.parse(result);

        String error = json.getAttribute(ApiConstants.RET_ERROR);
        if (error != null) {
            // Special treatment for ERROR_INVALID_TOKEN
            if (error.equals(ApiConstants.ERROR_INVALID_TOKEN)) {
                // Call the invalid token callback
                if (_tokenInvalidCallback != null) {
                    _tokenInvalidCallback.run();
                }
                throw new InvalidTokenApiException();
            }
            // Generic API error
            throw new ApiException(error);
        }
        // Check for unlocked medals
        String newMedal = json.getAttribute(ApiConstants.RET_NEW_MEDAL);
        if (newMedal != null) {
            if (_medalUnlockedCallback != null) {
                _medalUnlockedCallback.unlocked(Integer.parseInt(newMedal));
            }
        }

        return json;
    }

    // Callback for invalid token errors
    public static void setInvalidTokenCallback(Runnable callback) {
        _tokenInvalidCallback = callback;
    }

    public interface IMedalUnlockedCallback {
        void unlocked(int medalId);
    }
    // Callback for unlocking medals
    public static void setMedalUnlockedCallback(IMedalUnlockedCallback callback) {
        _medalUnlockedCallback = callback;
    }


    // Common checks for all services
    protected void expectOkStatus(JsonResult result) {
        // Parse result
        String status = result.getAttribute(ApiConstants.RET_STATUS);
        if (status == null || !status.equals(ApiConstants.STATUS_OK)) {
            // This should never happen, the API should always return an ok status or an error
            throwInvalidResponseError(result, ApiConstants.RET_STATUS);
        }
    }

    protected void assertResultNotNull(Object parsedObject, JsonResult result) {
        if (parsedObject == null) {
            // This should never happen, the API should always return a correct result or an error
            throwInvalidResponseError(result, ApiConstants.RET_RESULT);
        }
    }

    private void throwInvalidResponseError(JsonResult result, String expectedAttr) {
        throw new RuntimeException("Invalid response from server: " + result.toString()
                + "\nExpected " + ApiConstants.RET_ERROR + " or attribute '" + expectedAttr + "'");
    }
}
