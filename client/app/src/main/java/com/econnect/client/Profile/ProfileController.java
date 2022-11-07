package com.econnect.client.Profile;


import android.view.View;

import com.econnect.API.Exceptions.ProfileIsPrivateException;
import com.econnect.API.ForumService;
import com.econnect.API.ProfileService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;


public class ProfileController {

    protected final ProfileFragment _fragment;
    private final int _userId;

    ProfileController(ProfileFragment fragment, int userId) {
        this._fragment = fragment;
        _userId = userId;
    }

    void getInfoUser() {
        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                // Call API
                ProfileService.User u = getUser();
                ExecutionThread.UI(_fragment, () -> {
                    _fragment.updateUI(u);
                    _fragment.enableInput(true);
                });
            }
            catch (ProfileIsPrivateException e) {
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, e.getMessage());
                    _fragment.requireActivity().finish();
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_get_user_info) + e.getMessage());
                });
            }
        });
    }

    // Override this method to change which user to show data from
    protected ProfileService.User getUser() {
        ProfileService service = ServiceFactory.getInstance().getProfileService();
        return service.getInfoOtherUser(_userId);
    }

    View.OnClickListener reportUser() {
        return view -> {
            // Show confirmation dialog
            PopupMessage.yesNoDialog(_fragment, _fragment.getString(R.string.report_user), _fragment.getString(R.string.want_to_report_user), (dialog, id) -> {
                // Execute in non-ui thread
                ExecutionThread.nonUI(()-> {
                    // Delete post
                    ProfileService service = ServiceFactory.getInstance().getProfileService();
                    try {
                        service.reportUser(_userId);
                        ExecutionThread.UI(_fragment, ()-> {
                            _fragment.requireActivity().finish();
                            PopupMessage.showToast(_fragment, _fragment.getString(R.string.user_reported));
                        });
                    }
                    catch (Exception e) {
                        ExecutionThread.UI(_fragment, ()-> PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_report_user) + "\n" + e.getMessage()));
                    }
                });
            });
        };
    }
}
