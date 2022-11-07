package com.econnect.API.HttpClient;

import com.econnect.API.ApiConstants;

import java.util.Map;
import java.util.TreeMap;

public class StubHttpClient implements HttpClient {
    private static final String EXPECTED_DOMAIN = ApiConstants.BASE_URL;
    
    @Override
    public String get(String path, Map<String, String> params) {
        path = checkDomain(path);
        if (params == null) {
            params = new TreeMap<String, String>();
        }
        checkNullParams(params);
        
        switch (path) {
            // ping
            case "/":
            case "":
                return "PES Econnect Root!";

            // Login
            case "/account/login":
                expectParamsExclusive(params, "email", "password");
                // For development purposes. Delete this line when done.
                if (equals(params, "email", "a")) {
                    return "{\"token\":\"okToken\"}";
                }
                
                if (equals(params, "email", "okEmail") && equals(params, "password", "okPassword")) {
                    return "{\"token\":\"okToken\"}";
                }
                if (equals(params, "email", "okEmailNoAdmin") && equals(params, "password", "okPassword")) {
                    return "{\"token\":\"okTokenNoAdmin\"}";
                }
                else if (equals(params, "email", "okEmail")) {
                    return "{\"error\":\"ERROR_USER_INCORRECT_PASSWORD\"}";
                }
                else {
                    return "{\"error\":\"ERROR_USER_NOT_FOUND\"}";
                }
            
            // Logout
            case "/account/logout":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{status: 'success'}";
                }
                
            // Get list of product types
            case "/products/types":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    // For each type, return the name and an array of questions
                    return "{\"result\":[{\"name\":\"type1\",\"questions\":[{\"questionid\":1,\"statement\":\"q1\"},{\"questionid\":2,\"statement\":\"q2\"},{\"questionid\":3,\"statement\":\"q3\"}]},{\"name\":\"type2\",\"questions\":[{\"questionid\":4,\"statement\":\"q4\"},{\"questionid\":5,\"statement\":\"q5\"},{\"questionid\":6,\"statement\":\"q6\"}]}]}";
                }
                
            // Get list of products
            case "/products":
                expectParamsExclusive(params, "token", "type");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                // For each product of this type, return the id, name, avgRating, imageUrl, manufacturer and type
                if (equals(params, "type", "")) {
                    // For each product, return the id, name, avgRating, imageURL, manufacturer and type
                    return "{\"result\":[{\"id\":1,\"name\":\"product1\",\"avgrating\":1.0,\"imageurl\":\"https://miro.medium.com/max/500/0*-ouKIOsDCzVCTjK-.png\",\"manufacturer\":\"manufacturer1\",\"type\":\"type1\"},{\"id\":2,\"name\":\"product2\",\"avgrating\":2.0,\"imageurl\":\"https://wallpapercave.com/wp/wp4676582.jpg\",\"manufacturer\":\"manufacturer2\",\"type\":\"type1\"},{\"id\":3,\"name\":\"product3\",\"avgRating\":3.0,\"imageurl\":\"imageUrl3\",\"manufacturer\":\"manufacturer3\",\"type\":\"type2\"},{\"id\":4,\"name\":\"product4\",\"avgRating\":4.0,\"imageurl\":\"imageUrl4\",\"manufacturer\":\"manufacturer4\",\"type\":\"type2\"}, {\"id\":5,\"name\":\"during\",\"avgRating\":1.0,\"imageurl\":\"imageUrl1\",\"manufacturer\":\"manufacturer1\",\"type\":\"type1\"}]}";
                }
                else {
                    return "{\"error\":\"ERROR_TYPE_NOT_EXISTS\"}";
                }

            // Get detailed info about product
            case "/products/0":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                return "{\"name\":\"product0\",\"imageURL\":\"imageUrl0\",\"manufacturer\":\"manufacturer0\",\"type\":\"type0\",\"questions\":[{\"text\":\"q0\",\"num_yes\":11,\"num_no\":12},{\"text\":\"q1\",\"num_yes\":21,\"num_no\":22},{\"text\":\"q2\",\"num_yes\":31,\"num_no\":32}],\"ratings\":[1,0,0,0,0,10]}";
                
            case "/products/2":
                expectParamsExclusive(params, "token");
                return "{\"error\":\"ERROR_PRODUCT_NOT_EXISTS\"}";
                
            // Get list of companies
            case "/companies":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    // For each company, return the id, name, avgRating, imageURL, lat and lon
                    return "{\"result\":[{\"id\":1,\"name\":\"company1\",\"avgrating\":1.0,\"imageurl\":\"https://wallpapercave.com/wp/wp4676582.jpg\",\"lat\":1.0,\"lon\":1.0},{\"id\":2,\"name\":\"company2\",\"avgrating\":2.0,\"imageurl\":\"http://www.company2.com/image.png\",\"lat\":2.0,\"lon\":2.0}]}";
                }

            // Get detailed info about company
            case "/companies/1":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                return "{\"imageURL\":\"https://company.com/img.png\",\"latitude\":12,\"longitude\":34,\"name\":\"test\",\"questions\":[{\"num_no\":1,\"num_yes\":2,\"text\":\"bon servei?\"},{\"num_no\":3,\"num_yes\":4,\"text\":\"q2\"}],\"ratings\":[1,2,3,4,5,6],\"type\":\"Company\"}";
                
            case "/companies/2":
                expectParamsExclusive(params, "token");
                return "{\"error\":\"ERROR_COMPANY_NOT_EXISTS\"}";



            // Get list of posts on the forum
            case "/posts":
                expectParams(params, "token", "n");
                int n = toInt(params, "n");
                if (n <= 0) {
                    return "{\"error\":\"invalid value of n\"}";
                }

                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else if (!params.containsKey("tag")) {
                    // For each post, return: postId, username, userId, medal, text, imageURL, likes, dislikes, userOption (1 or 0), timestamp, ownpost and authorbanned
                    return "{\"result\":[{\"postid\":1,\"username\":\"user1\",\"userid\":1,\"medal\":\"1\",\"text\":\"#tag1 text1\",\"imageurl\":\"https://wallpapercave.com/wp/wp4676582.jpg\",\"likes\":1,\"dislikes\":2,\"useroption\":1,\"timestamp\":\"1649663866\",\"ownpost\":true,\"authorbanned\":false}," +
                            "{\"postid\":2,\"username\":\"user2\",\"userid\":2,\"medal\":\"2\",\"text\":\"text2 #another . #tag2\",\"imageurl\":\"image2\",\"likes\":3,\"dislikes\":4,\"useroption\":0,\"timestamp\":\"1649663810\",\"ownpost\":false,\"authorbanned\":true}," +
                            "{\"postid\":3,\"username\":\"Another User\",\"userid\":3,\"medal\":\"3\",\"text\":\"Post without tags.\",\"imageurl\":\"https://images.unsplash.com/photo-1559583985-c80d8ad9b29f\",\"likes\":1234,\"dislikes\":1234,\"useroption\":2,\"timestamp\":\"1649836904\",\"ownpost\":true,\"authorbanned\":false}]}";
                }
                else if (equals(params, "tag", "tag1")) {
                    return "{\"result\":[{\"postid\":1,\"username\":\"user1\",\"userid\":1,\"medal\":\"1\",\"text\":\"#tag1 text1\",\"imageurl\":\"https://wallpapercave.com/wp/wp4676582.jpg\",\"likes\":1,\"dislikes\":2,\"useroption\":1,\"timestamp\":\"1649663866\",\"ownpost\":true,\"authorbanned\":false}]}";
                }
                else if (equals(params, "tag", "tag2")) {
                    return "{\"result\":[{\"postid\":2,\"username\":\"user2\",\"userid\":2,\"medal\":\"2\",\"text\":\"text2 #another . #tag2\",\"imageurl\":\"image2\",\"likes\":3,\"dislikes\":4,\"useroption\":0,\"timestamp\":\"1649663810\",\"ownpost\":false,\"authorbanned\":true}]}";
                }
                else {
                    return "{\"result\":[]}";
                }

            // Get list of tags
            case "/posts/tags":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    // For each tag, return: tag, count
                    return "{\"result\": [{\"tag\":\"tag1\",\"count\":15},{\"tag\":\"tag2\",\"count\":2},{\"tag\":\"abc\",\"count\":1}]}";
                }

            //Get logged user info
            case "/account":
                expectParamsExclusive(params, "token");
                return "{\"result\":{\"username\":\"user1\",\"medals\":[{\"idmedal\":1,\"medalname\":\"testMedal\"},{\"idmedal\":2,\"medalname\":\"testMedal2\"}],\"activeMedal\":1,\"home\":null,\"email\":\"user1@gmail.com\",\"isPrivate\":true}}";

            default:
                throw new RuntimeException("Invalid path: " + path);

            case "/users/2":
                expectParamsExclusive(params, "token");
                return "{\"username\":\"user2\",\"medals\":[{\"idmedal\":1,\"medalname\":\"testMedal\"},{\"idmedal\":2,\"medalname\":\"testMedal2\"}],\"activeMedal\":1234,\"home\":null,\"email\":null,\"isPrivate\":null}";

            case "/users/3":
                expectParamsExclusive(params, "token");
                return "{\"error\":\"ERROR_PRIVATE_USER\"}";

            // Get logged user home coordinates
            case "/account/home":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else if (equals(params, "token", "userNoHome")) {
                    return "{\"lat\":null,\"lon\":null}";
                } else {
                    return "{\"lat\":12,\"lon\":34}";
                }
        }
    }

    @Override
    public String post(String path, Map<String, String> params, String json) {
        path = checkDomain(path);
        if (params == null) {
            params = new TreeMap<String, String>();
        }
        checkNullParams(params);
        
        switch (path) {
            // Create a new account
            case "/account":
                expectParamsExclusive(params, "username", "email", "password");
                if (equals(params, "username", "existingName")) {
                    return "{\"error\":\"ERROR_USERNAME_EXISTS\"}";
                }
                else if (equals(params, "email", "existingEmail")) {
                    return "{\"error\":\"ERROR_EMAIL_EXISTS\"}";
                }
                else {
                    return "{\"token\":\"okToken\"}";
                }
                
            case "/posts/1/like":
            case "/posts/2/like":
                expectParamsExclusive(params, "token", "isLike", "remove");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{\"status\":\"success\"}";
                }
            case "/posts/3/like":
                expectParamsExclusive(params, "token", "isLike", "remove");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{\"error\":\"ERROR_POST_NOT_EXISTS\"}";
                }

            case "/products/1/review":
                expectParams(params, "token", "review");
                return "{\"status\":\"success\"}";

            case "/products/2/review":
                expectParams(params, "token", "review");
                return "{\"error\":\"ERROR_PRODUCT_NOT_EXISTS\"}";

            case "/products/1/answer":
                expectParams(params, "token", "questionIndex", "chosenOption");
                return "{\"status\":\"success\"}";

            case "/companies/1/answer":
                expectParams(params, "token", "questionIndex", "chosenOption");
                return "{\"status\":\"success\"}";

            case "/products/2/answer":
                expectParams(params, "token", "questionIndex", "chosenOption");
                return "{\"error\":\"ERROR_PRODUCT_NOT_EXISTS\"}";

            case "/companies/2/answer":
                expectParams(params, "token", "questionIndex", "chosenOption");
                return "{\"error\":\"ERROR_PRODUCT_NOT_EXISTS\"}";

            default:
                throw new RuntimeException("Invalid path: " + path);
        }
        
    }
    
    
    @Override
    public String put(String path, Map<String, String> params, String json) {
        path = checkDomain(path);
        if (params == null) {
            params = new TreeMap<String, String>();
        }
        checkNullParams(params);
        
        switch (path) {
            case "/account/username":
                expectParams(params, "token", "newUsername");
                if (equals(params, "newUsername", "userExistent")) {
                    return "{\"error\":\"ERROR_USERNAME_EXISTS\"}";
                }
                else {
                    return "{status: 'success'}";
                }
            case "/account/email":
                expectParams(params, "token", "newEmail");
                if (equals(params, "newEmail", "emailExistent")) {
                    return "{\"error\":\"ERROR_USER_EMAIL_EXISTS\"}";
                }
                else if (equals(params, "newEmail", "emailInvalid")) {
                    return "{\"error\":\"ERROR_INVALID_EMAIL\"}";
                }
                else {
                    return "{status: 'success'}";
                }
            case "/account/medal":
                expectParams(params, "token", "medalId");
                if (equals(params, "medalId", "5")) {
                    return "{\"error\":\"ERROR_INVALID_MEDAL\"}";
                }
                else {
                    return "{status: 'success'}";
                }
            case "/account/visibility":
                expectParams(params, "token", "isPrivate");
                return "{status: 'success'}";
            case "/account/password":
                expectParams(params, "token", "oldPassword", "newPassword");
                if (!equals(params, "oldPassword", "123")) {
                    return "{\"error\":\"ERROR_INCORRECT_PASSWORD\"}";
                }
                else {
                    return "{status: 'success'}";
                }
            default:
                throw new RuntimeException("Invalid path: " + path);
        }
    }


    @Override
    public String delete(String path, Map<String, String> params) {
        path = checkDomain(path);
        if (params == null) {
            params = new TreeMap<String, String>();
        }
        checkNullParams(params);

        switch (path) {
            // Delete a post
            case "/posts/1":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{status: 'success'}";
                }
            case "/posts/2":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{\"error\":\"ERROR_USER_NOT_POST_OWNER\"}";
                }
            case "/posts/3":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{\"error\":\"ERROR_POST_NOT_EXISTS\"}";
                }
            case "/account":
                expectParamsExclusive(params, "token");
                if (equals(params, "token", "badToken")) {
                    return "{\"error\":\"ERROR_INVALID_TOKEN\"}";
                }
                else {
                    return "{status: 'success'}";
                }

            default:
                throw new RuntimeException("Invalid path: " + path);
        }
    }
    
    
    // CHECKS
    
    // Throw an exception if the path doesn't start with the expected domain
    private String checkDomain(String path) {
        if (!path.startsWith(EXPECTED_DOMAIN)) {
            throw new IllegalArgumentException("Path must start with " + EXPECTED_DOMAIN);
        }
        return path.substring(EXPECTED_DOMAIN.length());
    }
    
    // Throw an exception if any of the params is null
    private void checkNullParams(Map<String, String> params) {
        for (String param : params.keySet()) {
            if (params.get(param) == null) {
                throw new IllegalArgumentException("Parameter " + param + " cannot be null");
            }
        }
    }
    
    // Throw an exception if params doesn't contain all of the expected params
    private void expectParams(Map<String, String> params, String... expected) {
        for (String param : expected) {
            if (!params.containsKey(param)) {
                throw new IllegalArgumentException("Missing parameter: " + param);
            }
        }
    }
    
    // Throw an exception if params does't contain EXACTLY all of the expected params
    private void expectParamsExclusive(Map<String, String> params, String... expected) {
        expectParams(params, expected);
        if (params.size() != expected.length) {
            throw new IllegalArgumentException("Too many parameters passed. Got: " + params.keySet() + ", expected: " + expected);
        }
    }
    
    // Check whether a parameter has a certain value
    boolean equals(Map<String, String> params, String param, String value) {
        if (!params.containsKey(param)) {
            throw new IllegalArgumentException("Missing parameter: " + param);
        }
        return params.get(param).equals(value);
    }

    int toInt(Map<String, String> params, String param) {
        if (!params.containsKey(param)) {
            throw new IllegalArgumentException("Missing parameter: " + param);
        }
        return Integer.parseInt(params.get(param));
    }
}
