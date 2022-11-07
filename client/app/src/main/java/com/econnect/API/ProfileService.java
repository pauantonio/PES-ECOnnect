package com.econnect.API;

import com.econnect.API.Exceptions.ApiException;
import com.econnect.API.Exceptions.ProfileIsPrivateException;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

import java.util.TreeMap;

public class ProfileService extends Service {

    // Only allow instantiating from ServiceFactory
    ProfileService() {}

    public static class Medal {
        public final int idmedal;
        public Medal (int idmedal) {
            this.idmedal = idmedal;
        }
    }

    public static class User {
        // Important: The name of these attributes must match the ones in the returned JSON
        public final String username;
        public final Medal[] medals;
        public final int activeMedal;
        public final String home;
        public final Boolean isPrivate;
        public final String email;
        public final String about;
        public final String pictureURL;

        public User(String username, int activeMedal, String email, String home, Medal[] medals, Boolean isPrivate, String about, String imageUser) {
            this.username = username;
            this.medals = medals;
            this.activeMedal = activeMedal;
            this.home = home;
            this.email = email;
            this.isPrivate = isPrivate;
            this.about = about;
            this.pictureURL = imageUser;
        }
    }

    public static class HomeCoords {
        public final double latitude;
        public final double longitude;
        public final String address;
        public HomeCoords(double lat, double lon, String addr) {
            latitude = lat;
            longitude = lon;
            address = addr;
        }
    }

    public User getInfoLoggedUser() {
        JsonResult result;
        // Call API
        super.needsToken = true;
        result = get(ApiConstants.ACCOUNT_PATH, null);
        // Parse result
        User user = result.getObject(ApiConstants.RET_RESULT, User.class);
        assertResultNotNull(user, result);
        return user;
    }

    public User getInfoOtherUser(int userId) {
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = get(ApiConstants.USERS_PATH + "/" + userId, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_PRIVATE_USER:
                    throw new ProfileIsPrivateException();
                default:
                    throw e;
            }
        }
        // Parse result
        String username = result.getObject("username", String.class);
        Medal[] medals = result.getArray("medals", Medal[].class);
        String about = result.getObject("about", String.class);
        String pictureURL = result.getObject("pictureURL", String.class);
        String activeMedal = result.getAttribute("activeMedal");
        if(activeMedal == null) activeMedal = "0";

        assertResultNotNull(username, result);
        assertResultNotNull(medals, result);

        return new User(username,Integer.parseInt(activeMedal), null, null, medals, null, about, pictureURL);
    }

    public void updateUsername(String text) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.NEW_USERNAME, text);

        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = put(ApiConstants.ACCOUNT_USERNAME_PATH, params, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_ACCOUNT_USERNAME_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.username_taken));
                default:
                    throw e;
            }
        }
        // Parse result
        super.expectOkStatus(result);
    }

    public void updatePassword(String oldP, String newP) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.OLD_USER_PASSWORD, oldP);
        params.put(ApiConstants.NEW_USER_PASSWORD, newP);
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = put(ApiConstants.ACCOUNT_PASSWORD_PATH, params, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_ACCOUNT_INCORRECT_PASSWORD:
                    throw new RuntimeException(Translate.id(R.string.old_password_incorrect));
                default:
                    throw e;
            }
        }
        // Parse result
        super.expectOkStatus(result);
    }

    public void updateEmail(String text) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.NEW_USER_EMAIL, text);
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = put(ApiConstants.ACCOUNT_EMAIL_PATH, params, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_EMAIL_EXISTS:
                    throw new RuntimeException(Translate.id(R.string.email_exists));
                case ApiConstants.ERROR_ACCOUNT_INVALID_EMAIL:
                    throw new RuntimeException(Translate.id(R.string.enter_valid_email));
                default:
                    throw e;
            }
        }
        // Parse result
        super.expectOkStatus(result);
    }

    public void updateAbout(String text) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.NEW_USER_ABOUT, text);
        // Call API
        super.needsToken = true;
        JsonResult result = put(ApiConstants.ACCOUNT_ABOUT_PATH, params, null);
        // Parse result
        super.expectOkStatus(result);
    }

    public void updateActiveMedal(int id) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.NEW_USER_MEDAL, String.valueOf(id));
        JsonResult result;
        try {
            // Call API
            super.needsToken = true;
            result = put(ApiConstants.ACCOUNT_MEDAL_PATH, params, null);
        } catch (ApiException e) {
            switch (e.getErrorCode()) {
                case ApiConstants.ERROR_INVALID_MEDAL:
                    throw new RuntimeException(Translate.id(R.string.incorrect_medal));
                default:
                    throw e;
            }
        }
        // Parse result
        super.expectOkStatus(result);
    }

    public void updateAccountVisibility(Boolean isPrivate) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.IS_PRIVATE_USER, isPrivate.toString());

        JsonResult result;
        // Call API
        super.needsToken = true;
        result = put(ApiConstants.ACCOUNT_VISIBILITY_PATH, params, null);
        // This endpoint does not throw exceptions

        // Parse result
        super.expectOkStatus(result);
    }

    public void updatePicture(String url) {
        // Add parameters
        TreeMap<String, String> params = new TreeMap<>();
        params.put(ApiConstants.NEW_PROFILE_PICTURE, url);

        JsonResult result;
        // Call API
        super.needsToken = true;
        result = put(ApiConstants.ACCOUNT_PICTURE_PATH, params, null);
        // This endpoint does not throw exceptions

        // Parse Result
        super.expectOkStatus(result);

    }

    public void deleteAccount() {
        // Call API
        super.needsToken = true;
        JsonResult result = delete(ApiConstants.ACCOUNT_PATH, null);

        // Parse result
        super.expectOkStatus(result);
    }

    public HomeCoords getHomeLocation() {
        // Call API
        super.needsToken = true;
        JsonResult result = get(ApiConstants.HOME_PATH, null);

        // Parse result
        Double lat = result.getObject("lat", Double.class);
        Double lon = result.getObject("lon", Double.class);
        String address = result.getAttribute("address");

        if (lat == null || lon == null) return null;
        return new HomeCoords(lat, lon, address);
    }

    public void reportUser(int userId) {
        // Call API
        super.needsToken = true;
        JsonResult result = post(ApiConstants.USERS_PATH + "/" + userId + "/report", null, null);

        // Parse result
        expectOkStatus(result);
    }
}
