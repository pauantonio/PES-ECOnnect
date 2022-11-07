package com.econnect.client.Profile;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.econnect.Utilities.FragmentContainerActivity;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class SetHomeActivity extends FragmentContainerActivity {
    public SetHomeActivity() {
        super(Translate.id(R.string.set_home));
    }

    @Override
    protected Fragment initializeFragment(Intent intent) {
        return new SetHomeFragment();
    }
}
