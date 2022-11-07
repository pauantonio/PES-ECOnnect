package com.econnect.client.Forum;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.econnect.Utilities.FragmentContainerActivity;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;


public class PostActivity extends FragmentContainerActivity {

    public PostActivity() {
        super(Translate.id(R.string.new_post));
    }

    @Override
    protected Fragment initializeFragment(Intent intent) {
        // No parameters required
        return new AddPostFragment();
    }
}
