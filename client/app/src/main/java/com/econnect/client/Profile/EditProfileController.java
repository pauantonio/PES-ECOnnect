package com.econnect.client.Profile;

import static com.econnect.Utilities.BitmapLoader.fromURL;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.econnect.API.ImageUpload.ImageService;
import com.econnect.API.ProfileService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.FileUtils;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;

import java.io.File;
import java.net.URL;

public class EditProfileController {

    private final EditFragment _fragment;

    EditProfileController(EditFragment fragment) {
        this._fragment = fragment;
    }

    public void changePassword(String oldP, String newP) {

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updatePassword(oldP, newP);
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, _fragment.getString(R.string.password_updated));
                    _fragment.clearPasswordFields();
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_update_password) + e.getMessage());
                });
            }
        });

    }

    public void changeIsPrivate(Boolean isPrivate) {

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updateAccountVisibility(isPrivate);
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_change_visibility) + e.getMessage());
                });
            }
        });

    }

    public void changeEmail(String email) {

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updateEmail(email);
                _fragment.updateEmail(email);
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_update_email) + e.getMessage());
                });
            }
        });
    }

    public void changeUsername(String username) {

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updateUsername(username);
                _fragment.updateUsername(username);
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_update_username) + e.getMessage());
                });
            }
        });
    }

    public void changeAbout(String about) {
        // This could take some time (and accesses the internet), run non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updateAbout(about);
                _fragment.updateAbout(about);
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_update_about) + e.getMessage());
                });
            }
        });
    }

    public void changeProfilePicture(Uri image) {
        // Get picture url
        ExecutionThread.nonUI(() ->{
            String url = getImageUrl(image);
            // Call Service
            ProfileService profileService = ServiceFactory.getInstance().getProfileService();
            profileService.updatePicture(url);
        });
    }

    private String getImageUrl(Uri image) {
        if (image == null)
            return "";
        File tempFile;
        try {
            tempFile = FileUtils.from(_fragment.requireContext(), image);
        } catch (Exception e) {
            throw new RuntimeException(_fragment.getString(R.string.could_not_convert_uri_file) + e.getMessage(), e);
        }

        final ImageService service = new ImageService();
        String url = service.uploadImageToUrl(tempFile);
        if (!isValidURL(url)) {
            throw new RuntimeException(_fragment.getString(R.string.invalid_generated_url) + url);
        }
        return url;
    }

    private static boolean isValidURL (String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeProfilePicture() {
        ExecutionThread.nonUI(() ->{
            // Call Service
            ProfileService profileService = ServiceFactory.getInstance().getProfileService();
            profileService.updatePicture(null);
            // remove image
            _fragment.removeProfileImage();
        });
    }
}
