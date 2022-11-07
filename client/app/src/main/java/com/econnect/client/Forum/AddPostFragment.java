package com.econnect.client.Forum;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.econnect.Utilities.CustomFragment;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentAddPostBinding;

import java.io.File;


public class AddPostFragment extends CustomFragment<FragmentAddPostBinding>  {

    private final AddPostController _ctrl = new AddPostController(this);
    private Uri _selectedImage = null;

    private final ActivityResultLauncher<String> _getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri == null) return;
        _selectedImage = uri;
        binding.imagePreview.setImageURI(uri);
        binding.removeImageButton.setVisibility(View.VISIBLE);
    });

    public AddPostFragment() {
        super(FragmentAddPostBinding.class);
    }

    @Override
    protected void addListeners() {
        new ActivityResultContracts.GetContent();

        binding.addPostButton.setOnClickListener(view -> _ctrl.addPostOnClick());
        binding.imagePreview.setOnClickListener(view -> _getContentLauncher.launch("image/*"));
        binding.removeImageButton.setOnClickListener(view -> {
            binding.removeImageButton.setVisibility(View.GONE);
            _selectedImage = null;
            binding.imagePreview.setImageResource(R.drawable.ic_add_24);
        });
    }

    void enableInput(boolean enabled) {
        binding.addPostButton.setEnabled(enabled);
        binding.removeImageButton.setEnabled(enabled);
        binding.imagePreview.setClickable(enabled);
        binding.newPostBox.setEnabled(enabled);
    }

    String getText() {
        return binding.textPost.getText().toString();
    }

    Uri getSelectedImage() {
        return _selectedImage;
    }
}
