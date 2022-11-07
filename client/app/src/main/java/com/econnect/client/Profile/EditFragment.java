package com.econnect.client.Profile;

import static com.econnect.Utilities.BitmapLoader.fromURL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.econnect.Utilities.BasicTextWatcher;
import com.econnect.Utilities.CustomFragment;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentEditProfileBinding;

public class EditFragment extends CustomFragment<FragmentEditProfileBinding> {

    private final EditProfileController _ctrl = new EditProfileController(this);
    private String _username;
    private String _email;
    private String _about;
    private final Boolean _isPrivate;
    private final String _pictureURL;

    private final ActivityResultLauncher<String> _getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri == null) return;
        binding.editUserImage.setImageURI(uri);
        binding.deleteImageButton.setEnabled(true);
        _ctrl.changeProfilePicture(uri);
        PopupMessage.showToast(this, getString(R.string.image_updated));
    });

    public EditFragment(String username, String email, String about,Boolean isPrivate, String pictureURL) {

        super(FragmentEditProfileBinding.class);
        this._username = username;
        this._email = email;
        this._about = about;
        this._isPrivate = isPrivate;
        this._pictureURL = pictureURL;
    }

    @Override
    protected void addListeners() {
        binding.changeNameButton.setOnClickListener(view ->
            _ctrl.changeUsername(binding.editUsernameText.getText().toString())
        );
        binding.changeEmailButton.setOnClickListener(view ->
            _ctrl.changeEmail(binding.editEmailText.getText().toString())
        );
        binding.changeAboutButton.setOnClickListener(view ->
            _ctrl.changeAbout(binding.editAboutText.getText().toString())
        );
        binding.switchPrivate.setOnClickListener(view ->
            _ctrl.changeIsPrivate(binding.switchPrivate.isChecked())
        );
        binding.editUserImage.setOnClickListener(view -> {
            _getContentLauncher.launch("image/*");
        });

        binding.deleteImageButton.setOnClickListener(view -> {
           _ctrl.removeProfilePicture();
           binding.deleteImageButton.setEnabled(false);
           PopupMessage.showToast(this, getString(R.string.image_updated));
        });

        binding.editUsernameText.addTextChangedListener(new BasicTextWatcher(() -> {
            boolean sameText = binding.editUsernameText.getText().toString().equals(_username);
            binding.changeNameButton.setEnabled(!sameText);
        }));
        binding.editEmailText.addTextChangedListener(new BasicTextWatcher(() -> {
            boolean sameText = binding.editEmailText.getText().toString().equals(_email);
            binding.changeEmailButton.setEnabled(!sameText);
        }));
        binding.editAboutText.addTextChangedListener(new BasicTextWatcher(() -> {
            boolean sameText = binding.editAboutText.getText().toString().equals(_about);
            binding.changeAboutButton.setEnabled(!sameText);
        }));

        binding.setHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), SetHomeActivity.class);
            requireActivity().startActivity(intent);
        });

        binding.changePassword.setOnClickListener(view -> changePassword());
        setDefaultValues();
    }

    private void changePassword() {
        String newPassword = binding.newPasswordText.getText().toString();
        String oldPassword = binding.oldPasswordText.getText().toString();
        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            PopupMessage.warning(this, getString(R.string.must_fill_passwords));
            return;
        }
        if (newPassword.equals(oldPassword)) {
            PopupMessage.warning(this, getString(R.string.password_conflict));
            return;
        }
        _ctrl.changePassword(oldPassword, newPassword);
    }

    void updateUsername(String newUsername) {
        _username = newUsername;
        ExecutionThread.UI(this, ()->binding.changeNameButton.setEnabled(false));

    }
    void updateEmail(String newEmail) {
        _email = newEmail;
        ExecutionThread.UI(this, ()->binding.changeEmailButton.setEnabled(false));
    }

    void updateAbout(String newAbout) {
        _about = newAbout;
        ExecutionThread.UI(this, ()->binding.changeAboutButton.setEnabled(false));
    }

    public void setDefaultValues() {
        binding.editUsernameText.setText(_username);
        binding.editEmailText.setText(_email);
        binding.editAboutText.setText(_about);
        binding.switchPrivate.setChecked(_isPrivate);
        // set image
        Drawable userDefaultImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_profile_24);
        ImageView image = binding.editUserImage;
        ExecutionThread.nonUI(()->{
            Bitmap bmp = fromURL(_pictureURL);
            ExecutionThread.UI(this, ()-> {
                if (bmp == null) image.setImageDrawable(userDefaultImage);
                else {
                    image.setImageBitmap(bmp);
                    binding.deleteImageButton.setEnabled(true);
                }
            });
        });
    }

    public void clearPasswordFields() {
        binding.oldPasswordText.setText("");
        binding.newPasswordText.setText("");
    }

    public void removeProfileImage() {
        Drawable userDefaultImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_profile_24);
        ImageView image = binding.editUserImage;
        ExecutionThread.UI(this, ()-> {
            image.setImageDrawable(userDefaultImage);
        });
    }
}
