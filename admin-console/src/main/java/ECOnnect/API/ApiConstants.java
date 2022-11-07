package ECOnnect.API;

// Namespace for storing all the API constants together.
public interface ApiConstants {
    
    // PATHS
    String BASE_URL_DEV = "http://127.0.0.1:5000";
    String BASE_URL_DEPLOY = "https://pes-econnect.herokuapp.com";
    String BASE_URL = BASE_URL_DEPLOY;
    
    String LOGIN_PATH = "/account/login";
    String LOGOUT_PATH = "/account/logout";
    String IS_ADMIN_PATH = "/account/isadmin";
    String TYPES_PATH = "/products/types";
    String QUESTIONS_PATH = "/questions";
    String PRODUCTS_PATH = "/products";
    String COMPANIES_PATH = "/companies";
    String COMPANY_QUESTIONS_PATH = "/companies/questions";
    String POSTS_PATH = "/posts";
    String BAN_PATH = "/users/%d/ban";
    
    
    // PARAMETERS
    
    // General
    String TOKEN = "token";
    
    // Login
    String ADMIN_LOGIN_NAME = "email";
    String ADMIN_LOGIN_PASSWORD = "password";
    
    // Get/create/delete product types
    String PRODUCT_TYPES_NAME = "name";
    String PRODUCT_TYPES_QUESTIONS = "questions";

    // Create question
    String QUESTION_STATEMENT = "statement";
    String QUESTION_TYPE = "type";

    
    // Edit product types and questions
    String PRODUCT_TYPES_OLD_NAME = "oldName";
    String PRODUCT_TYPES_NEW_NAME = "newName";
    String QUESTION_NEW_STATEMENT = "newQuestion";
    
    // Get/create products
    String PRODUCT_NAME = "name";
    String PRODUCT_MANUFACTURER = "manufacturer";
    String PRODUCT_IMAGE_URL = "imageURL";
    String PRODUCT_TYPE = "type";
    
    // Get/create companies
    String COMPANY_NAME = "name";
    String COMPANY_IMAGE_URL = "imageURL";
    String COMPANY_LOCATION_LAT = "lat";
    String COMPANY_LOCATION_LON = "lon";
    
    // Get/delete posts
    String POST_AMOUNT = "n";
    String POST_TAG = "tag";
    String POST_ID = "id";
    
    // Ban user
    String BAN_SET_BANNED = "isBanned";
    
    
    // RETURN VALUES
    
    String RET_TOKEN = "token";
    String RET_STATUS = "status";
    String STATUS_OK = "success";
    String RET_ERROR = "error";
    String RET_RESULT = "result";
    
    
    // ERRORS
    
    // General
    String ERROR_INVALID_TOKEN = "ERROR_INVALID_TOKEN";
    
    // Login
    String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    String ERROR_WRONG_PASSWORD = "ERROR_USER_INCORRECT_PASSWORD";
    
    // Create product type
    String ERROR_TYPE_EXISTS = "ERROR_TYPE_EXISTS";
    
    // Get/create/delete products
    String ERROR_TYPE_NOT_EXISTS = "ERROR_TYPE_NOT_EXISTS";
    String ERROR_PRODUCT_EXISTS = "ERROR_PRODUCT_EXISTS";
    String ERROR_PRODUCT_NOT_EXISTS = "ERROR_PRODUCT_NOT_EXISTS";
    
    // Get/create/delete companies
    String ERROR_COMPANY_EXISTS = "ERROR_COMPANY_EXISTS";
    String ERROR_COMPANY_NOT_EXISTS = "ERROR_COMPANY_NOT_EXISTS";
    
    // Delete posts
    String ERROR_POST_NOT_EXISTS = "ERROR_POST_NOT_EXISTS";
    
    // Ban user
    String ERROR_USER_NOT_EXISTS = "ERROR_USER_NOT_EXISTS";
    String ERROR_CANNOT_BAN_YOURSELF = "ERROR_CANNOT_BAN_YOURSELF";
    
    // Edit/delete question
    String ERROR_QUESTION_NOT_EXISTS = "ERROR_QUESTION_NOT_EXISTS";
}
