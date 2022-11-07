package com.econnect.client.Profile;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.econnect.Utilities.FragmentContainerActivity;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class EditProfileActivity extends FragmentContainerActivity {

    public EditProfileActivity() {
        super(Translate.id(R.string.edit_profile));
    }

    @Override
    protected Fragment initializeFragment(Intent intent) {
        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String about = intent.getStringExtra("about");
        Boolean isPrivate = intent.getBooleanExtra("isPrivate", false);
        String pictureURL = intent.getStringExtra("pictureURL");
        return new EditFragment(username, email, about,isPrivate, pictureURL);

    }

}
