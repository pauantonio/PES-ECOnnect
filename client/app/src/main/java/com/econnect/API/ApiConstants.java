package com.econnect.API;

// Namespace for storing all the API constants together.
public interface ApiConstants {
    // PATHS
    String BASE_URL_DEV = "http://10.0.2.2:5000";
    String BASE_URL_DEPLOY = "https://pes-econnect.herokuapp.com";
    String BASE_URL = BASE_URL_DEPLOY;

    String LOGIN_PATH = "/account/login";
    String LOGOUT_PATH = "/account/logout";
    String ACCOUNT_PATH = "/account";
    String HOME_PATH = "/account/home";
    String USERS_PATH = "/users";
    String TYPES_PATH = "/products/types";
    String PRODUCTS_PATH = "/products";
    String COMPANIES_PATH = "/companies";
    String COMPANY_QUESTIONS_PATH = "/companies/questions";
    String POSTS_PATH = "/posts";
    String TAGS_PATH = "/posts/tags";
    String POST_LIKE_PATH = "/posts/%d/like";
    String ACCOUNT_USERNAME_PATH = "/account/username";
    String ACCOUNT_EMAIL_PATH = "/account/email";
    String ACCOUNT_ABOUT_PATH = "/account/about";
    String ACCOUNT_PASSWORD_PATH = "/account/password";
    String ACCOUNT_MEDAL_PATH = "/account/medal";
    String ACCOUNT_VISIBILITY_PATH = "/account/visibility";
    String ACCOUNT_PICTURE_PATH = "/account/picture";


    // PARAMETERS
    // General
    String TOKEN = "token";

    // Login
    String LOGIN_NAME = "email";
    String LOGIN_PASSWORD = "password";

    // Register
    String REGISTER_NAME = "username";
    String REGISTER_PASSWORD = "password";
    String REGISTER_EMAIL = "email";

    // Get/create products
    String PRODUCT_TYPE = "type";

    // Product
    String PRODUCT_ID = "productId";

    // Review
    String REVIEW = "review";

    // Question
    String ANSWER = "answer";
    String QUESTION_INDEX = "questionIndex";
    String CHOSEN_OPTION = "chosenOption";

    // Get/like/delete posts
    String POST_AMOUNT = "n";
    String POST_TAG = "tag";
    String POST_IS_LIKE = "isLike";
    String POST_REMOVE = "remove";
    String POST_TEXT = "text";
    String POST_IMAGE = "image";

    //PUT attributes User
    String NEW_USER_MEDAL = "medalId";
    String NEW_USERNAME = "newUsername";
    String NEW_USER_EMAIL = "newEmail";
    String NEW_USER_ABOUT = "newAbout";
    String NEW_USER_PASSWORD = "newPassword";
    String OLD_USER_PASSWORD = "oldPassword";
    String IS_PRIVATE_USER = "isPrivate";
    String NEW_PROFILE_PICTURE = "newPictureURL";

    //GET paths Home
    String BUILDING_PATH = "/homes/building";
    String CITY_PATH= "/homes/cities";

    //GET attributes Home
    String ZIPCODE = "zipcode";
    String STREET_NAME= "street";
    String STREET_NUM = "number";
    String ESCALA = "escala";
    String FLOOR = "pis";
    String DOOR = "porta";

    // RETURN VALUES
    String RET_TOKEN = "token";
    String RET_STATUS = "status";
    String STATUS_OK = "success";
    String RET_ERROR = "error";
    String RET_RESULT = "result";
    String RET_NEW_MEDAL = "medal";

    
    // ERRORS
    // General
    String ERROR_INVALID_TOKEN = "ERROR_INVALID_TOKEN";

    // Login
    String ERROR_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    String ERROR_WRONG_PASSWORD = "ERROR_USER_INCORRECT_PASSWORD";
    String ERROR_BANNED = "ERROR_BANNED";

    // Sign up
    String ERROR_USERNAME_EXISTS = "ERROR_USER_USERNAME_EXISTS";
    String ERROR_EMAIL_EXISTS = "ERROR_USER_EMAIL_EXISTS";

    // Get products/companies
    String ERROR_TYPE_NOT_EXISTS = "ERROR_TYPE_NOT_EXISTS";
    String ERROR_PRODUCT_NOT_EXISTS = "ERROR_PRODUCT_NOT_EXISTS";
    String ERROR_COMPANY_NOT_EXISTS = "ERROR_COMPANY_NOT_EXISTS";

    // Delete posts
    String ERROR_POST_NOT_EXISTS = "ERROR_POST_NOT_EXISTS";
    String ERROR_USER_NOT_POST_OWNER = "ERROR_USER_NOT_POST_OWNER";

    //Post posts
    String ERROR_INCORRECT_INSERTION = "ERROR_INCORRECT_INSERTION";

    // Attributes of user
    String ERROR_INVALID_MEDAL = "ERROR_INVALID_MEDAL";
    String ERROR_PRIVATE_USER = "ERROR_PRIVATE_USER";

    // Update account data
    String ERROR_ACCOUNT_INCORRECT_PASSWORD = "ERROR_INCORRECT_PASSWORD";
    String ERROR_ACCOUNT_INVALID_EMAIL = "ERROR_INVALID_EMAIL";
    String ERROR_ACCOUNT_USERNAME_EXISTS = "ERROR_USERNAME_EXISTS";

    // Setting Home Errors
    String ERROR_CITY_NOT_EXISTS = "CITY_NOT_EXISTS";
    String ERROR_BUILDING_NOT_EXISTS = "BUILDING_NOT_EXISTS";
}
