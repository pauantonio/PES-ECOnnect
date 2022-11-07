package com.econnect.client.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.econnect.Utilities.FragmentContainerActivity;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class VisitUserProfileActivity extends FragmentContainerActivity {

    public VisitUserProfileActivity() {
        super(Translate.id(R.string.user_profile));
    }

    @Override
    protected Fragment initializeFragment(Intent intent) {
        int userId = intent.getIntExtra("username", -1);
        if (userId == -1) {
            throw new RuntimeException("VisitUserProfileActivity requires a userId");
        }
        return new ProfileFragment(userId);
    }
}
