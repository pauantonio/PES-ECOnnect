package ECOnnect.API;

import java.util.TreeMap;

import ECOnnect.API.Exceptions.ApiException;

public class LoginService extends Service {
    
    // Only allow instantiating from ServiceFactory
    LoginService() {}
    
    // Sets the user token, throws an exception if an error occurs or the user is not admin
    public void login(String email, String password) {
        
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.ADMIN_LOGIN_NAME, email);
        params.put(ApiConstants.ADMIN_LOGIN_PASSWORD, password);
        
        // Call API
        super.needsToken = false;
        JsonResult result;
        
        try {
            result = get(ApiConstants.LOGIN_PATH, params);
        }
        catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_USER_NOT_FOUND:
                    throw new RuntimeException("No account found for this email");
                case ApiConstants.ERROR_WRONG_PASSWORD:
                    throw new RuntimeException("Incorrect password for this email");
                default:
                    throw e;
            }
        }
        
        String token = result.getAttribute(ApiConstants.RET_TOKEN);
        assertResultNotNull(token, result);
        
        Service.setToken(token);
        
        checkIsAdmin();
    }
    
    // Throws an exception if the logged in user is not an admin
    private void checkIsAdmin() {
        try {
            // Call API (no parameters needed)
            super.needsToken = true;
            JsonResult result = get(ApiConstants.IS_ADMIN_PATH, null);
            String isAdmin = result.getAttribute(ApiConstants.RET_RESULT);
            assertResultNotNull(isAdmin, result);
            
            if (!isAdmin.equals("true")) {
                throw new RuntimeException("This user is not an admin");
            }
        }
        catch (Exception e) {
            // If an exception is thrown, the user is not an admin
            logout();
            throw e;
        }
    }
    
    // Invalidates the user token, throws an exception if an error occurs
    public void logout() {
        try {
            // Call API to invalidate in server (no parameters needed)
            super.needsToken = true;
            get(ApiConstants.LOGOUT_PATH, null);
        }
        finally {
            // Delete local token whether or not the API call succeeded
            try {
                Service.deleteAdminToken();
            }
            catch (Exception e) {
                // Tried to delete the token, but it was already deleted
            }
        }
    }
    
    // Wake up the server
    public void pingServer() {
        // Call API (no parameters needed)
        final String EXPECTED_RESPONSE = "PES Econnect Root!";
        String result = null;
        try {
            result = getRaw(ApiConstants.BASE_URL, null);
        }
        catch (Exception e) {
            System.err.println("Error sending ping to server:\n" + e.getMessage());
            return;
        }
        
        if (result == null) {
            System.err.println("Warning: Server is not responding to ping");
            return;
        }
        if (!result.equals(EXPECTED_RESPONSE)) {
            System.err.println("Warning: Server returned invalid response to ping '" + result + "'");
            System.err.println("(expected '" + EXPECTED_RESPONSE + "')");
        }
    }
}
