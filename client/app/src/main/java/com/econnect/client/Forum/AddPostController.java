package com.econnect.client.Forum;


import android.app.Activity;
import android.net.Uri;

import com.econnect.API.ForumService;
import com.econnect.API.ImageUpload.ImageService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.FileUtils;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;

import java.io.*;
import java.net.URL;


public class AddPostController {

    private final AddPostFragment _fragment;

    public AddPostController(AddPostFragment fragment) {
        this._fragment = fragment;
    }

    public void addPostOnClick() {
        //GET text and image
        String text_post = _fragment.getText();

        // Local validation
        if (text_post.isEmpty()) {
            PopupMessage.warning(_fragment, _fragment.getString(R.string.must_fill_text));
            return;
        }
        _fragment.enableInput(false);

        // This could take some time (and accesses the internet), run on non-UI thread
        ExecutionThread.nonUI(() -> {
            try {
                attemptPost(text_post, getImageUrl());
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    _fragment.enableInput(true);
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_create_post) + e.getMessage());
                });
            }
        });
    }

    private String getImageUrl() {
        Uri image = _fragment.getSelectedImage();
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
    private static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void attemptPost(String text, String url) {
        // Post
        ForumService postService = ServiceFactory.getInstance().getForumService();
        postService.createPost(text, url);
        // Success
        ExecutionThread.UI(_fragment, ()->{
            _fragment.enableInput(true);
            _fragment.requireActivity().setResult(Activity.RESULT_OK);
            _fragment.requireActivity().finish();
        });
    }
}
