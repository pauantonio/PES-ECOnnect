package com.econnect.client.Profile;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.econnect.API.LoginService;
import com.econnect.API.ProfileService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.SettingsFile;
import com.econnect.client.R;
import com.econnect.client.StartupActivity;


public class LoggedUserProfileController extends ProfileController {

    private final ActivityResultLauncher<Intent> _activityLauncher;
    // Store user to initialize edit fields without calling API again
    private ProfileService.User u;


    LoggedUserProfileController(ProfileFragment fragment) {
        super(fragment, -1);
        _activityLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::launchDetailsCallback
        );
    }

    @Override
    protected ProfileService.User getUser() {
        ProfileService profileService = ServiceFactory.getInstance().getProfileService();
        u = profileService.getInfoLoggedUser();
        return u;
    }

    private void launchDetailsCallback(ActivityResult result) {
        // Called once the user returns from edit screen
        // No need to refresh data, it's reloaded every time that the fragment is selected
    }

    public void logoutButtonClick() {
        // Show dialog
        PopupMessage.yesNoDialog(_fragment, _fragment.getString(R.string.log_out), _fragment.getString(R.string.want_to_logout), (dialog, id) -> {
            // If YES option is selected:
            ExecutionThread.nonUI(() -> {
                // Logout
                LoginService loginService = ServiceFactory.getInstance().getLoginService();
                try {
                    loginService.logout();
                    ExecutionThread.UI(_fragment, ()->{
                        _fragment.requireActivity().finish();
                    });
                }
                catch (Exception e) {
                    // Return to UI for showing errors
                    ExecutionThread.UI(_fragment, ()->{
                        // Even if there has been an error, return to login anyways
                        _fragment.requireActivity().finish();
                    });
                }
            });
        });
    }

    public void editButtonClick() {
        // Launch new activity PostActivity
        Intent intent = new Intent(_fragment.requireContext(), EditProfileActivity.class);
        // Pass parameters to activity
        intent.putExtra("username", u.username);
        intent.putExtra("email", u.email);
        intent.putExtra("about", u.about);
        intent.putExtra("isPrivate", u.isPrivate);
        intent.putExtra("pictureURL", u.pictureURL);

        _activityLauncher.launch(intent);
    }

    public void changeActiveMedal(int id) {

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.updateActiveMedal(id);
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_update_medal) + e.getMessage());
                });
            }
        });
    }

    public void deleteAccount() {
        ExecutionThread.nonUI(() -> {
            try {
                // Delete account
                ProfileService profileService = ServiceFactory.getInstance().getProfileService();
                profileService.deleteAccount();
                // Delete stored token
                LoginService loginService = ServiceFactory.getInstance().getLoginService();
                loginService.localLogout();

                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, _fragment.getString(R.string.account_deleted));
                    _fragment.requireActivity().finish();
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_delete_account) + e.getMessage());
                });
            }
        });
    }

    enum LocaleLanguage {
        ENGLISH,
        SPANISH,
        CATALAN
    }
    public void changeLanguage(LocaleLanguage language) {
        final SettingsFile file = new SettingsFile(_fragment);

        // ISO 639 language codes
        final String ENGLISH_CODE = "en";
        final String SPANISH_CODE = "es";
        final String CATALAN_CODE = "ca";

        switch (language) {
            case ENGLISH:
                file.putString(StartupActivity.CUSTOM_LANGUAGE_KEY, ENGLISH_CODE);
                break;
            case SPANISH:
                file.putString(StartupActivity.CUSTOM_LANGUAGE_KEY, SPANISH_CODE);
                break;
            case CATALAN:
                file.putString(StartupActivity.CUSTOM_LANGUAGE_KEY, CATALAN_CODE);
                break;
            default:
                throw new RuntimeException("Unrecognized language");
        }
        
        PopupMessage.showToast(_fragment, _fragment.getString(R.string.language_applied_when_restarted));
    }
}
