package com.econnect.Utilities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.econnect.client.R;

public abstract class FragmentContainerActivity extends AppCompatActivity {

    private final String _activityTitle;

    public FragmentContainerActivity(String activityTitle) {
        _activityTitle = activityTitle;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // Init Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        setTitle(_activityTitle);

        // Enable back arrow in title bar
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize and display fragment
        Fragment fragment = initializeFragment(getIntent());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainerMainLayout, fragment).commit();
    }

    protected abstract Fragment initializeFragment(Intent intent);

    // If back arrow in title bar is pressed, finish activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
