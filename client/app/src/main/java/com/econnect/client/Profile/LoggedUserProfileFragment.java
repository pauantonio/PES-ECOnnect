package com.econnect.client.Profile;

import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.econnect.API.ProfileService;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.Profile.Medals.MedalUtils;
import com.econnect.client.R;

public class LoggedUserProfileFragment extends ProfileFragment {

    // This controller is instantiated using instantiateController(), we can cast it to LoggedUserProfileController
    protected final LoggedUserProfileController _ctrl = (LoggedUserProfileController) super._ctrl;

    @Override
    protected ProfileController instantiateController() {
        return new LoggedUserProfileController(this);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        // Show floating button for logged user profile
        binding.profileMenuButton.setVisibility(View.VISIBLE);
        binding.profileMenuButton.setOnClickListener(view -> profileMenuClicked());
        // Add click listener to medal list
        binding.medalsList.setOnItemClickListener(createActiveDialog());
        // Show locked medals list
        binding.lockedMedalsText.setVisibility(View.VISIBLE);
        binding.lockedMedalsList.setVisibility(View.VISIBLE);
        binding.reportButton.setVisibility(View.GONE);
    }

    @Override
    void enableInput(boolean enabled) {
        super.enableInput(enabled);
        binding.profileMenuButton.setEnabled(enabled);
        binding.medalsList.setEnabled(enabled);
    }

    void profileMenuClicked(){
        PopupMessage.showPopupMenu(R.menu.profile_menu, binding.profileMenuButton, menuItem -> {
            // Called when an item is selected
            int itemId = menuItem.getItemId();
            if (itemId == R.id.profile_logout) {
                _ctrl.logoutButtonClick();
            }
            else if (itemId == R.id.profile_edit) {
                _ctrl.editButtonClick();
            }
            else if (itemId == R.id.profile_delete_account) {
                createDeleteAccountDialog();
            }
            else if (itemId == R.id.profile_translate) {
                createTranslateDialog();
            }
            else {
                throw new RuntimeException("Unrecognized menu option");
            }
            return true;
        });
    }

    private void createTranslateDialog() {
        AlertDialog.Builder languageBuilder = new AlertDialog.Builder(requireContext());

        final View languagePopupView = getLayoutInflater().inflate(R.layout.change_language, null);

        Button englishButton = languagePopupView.findViewById(R.id.englishButton);
        Button spanishButton = languagePopupView.findViewById(R.id.spanishButton);
        Button catalanButton = languagePopupView.findViewById(R.id.catalanButton);

        languageBuilder.setView(languagePopupView);
        final AlertDialog languageDialog = languageBuilder.create();
        languageDialog.show();

        englishButton.setOnClickListener(view -> {
            _ctrl.changeLanguage(LoggedUserProfileController.LocaleLanguage.ENGLISH);
            languageDialog.dismiss();
        });
        spanishButton.setOnClickListener(view -> {
            _ctrl.changeLanguage(LoggedUserProfileController.LocaleLanguage.SPANISH);
            languageDialog.dismiss();
        });
        catalanButton.setOnClickListener(view -> {
            _ctrl.changeLanguage(LoggedUserProfileController.LocaleLanguage.CATALAN);
            languageDialog.dismiss();
        });

    }

    public void createDeleteAccountDialog() {
        AlertDialog.Builder deleterBuilder = new AlertDialog.Builder(requireContext());

        final View deleterPopupView = getLayoutInflater().inflate(R.layout.delete_account, null);

        Button deleteButton = deleterPopupView.findViewById(R.id.deleteAccountButton);
        Button cancelButton = deleterPopupView.findViewById(R.id.deleteAccountCancel);

        deleterBuilder.setView(deleterPopupView);
        AlertDialog deleter = deleterBuilder.create();
        deleter.show();

        deleteButton.setOnClickListener(view -> {
            TextView confirmation = deleterPopupView.findViewById(R.id.deleteAccountConfirmation);
            String accept = getString(R.string.i_accept_text);
            if (!confirmation.getText().toString().equals(accept)) {
                PopupMessage.warning(this, getString(R.string.type_exactly, accept));
                return;
            }
            _ctrl.deleteAccount();
            deleter.dismiss();
        });

        cancelButton.setOnClickListener(view -> {
            deleter.dismiss();
        });
    }

    private AdapterView.OnItemClickListener createActiveDialog() {

        return (parent, view, position, id) -> {
            final AlertDialog.Builder medalBuilder = new AlertDialog.Builder(requireContext());
            final View medalPopupView = getLayoutInflater().inflate(R.layout.set_active_medal, null);
            medalBuilder.setView(medalPopupView);
            final AlertDialog review = medalBuilder.create();
            review.show();

            final Button yes_option = medalPopupView.findViewById(R.id.yesChangeActiveMedal);
            final Button no_option = medalPopupView.findViewById(R.id.noChangeActiveMedal);

            no_option.setOnClickListener(View -> review.dismiss());
            yes_option.setOnClickListener(View -> {
                ProfileService.Medal m = (ProfileService.Medal) parent.getItemAtPosition(position);
                ExecutionThread.nonUI(() -> {
                    _ctrl.changeActiveMedal(m.idmedal);
                    ExecutionThread.UI(this, ()-> {
                        binding.idMedalText.setText(MedalUtils.medalName(m.idmedal));
                        binding.medalActiveImage.setImageDrawable(MedalUtils.medalIcon(m.idmedal));
                    });
                });
                review.dismiss();

            });

        };
    }

    @Override
    protected boolean shouldAddNullMedal() {
        // Show "no medal item"
        return true;
    }
}
