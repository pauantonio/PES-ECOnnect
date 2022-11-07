package com.econnect.client;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.econnect.API.Service;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.Companies.CompaniesFragment;
import com.econnect.client.Forum.ForumFragment;
import com.econnect.client.Products.ProductsFragment;
import com.econnect.client.Profile.LoggedUserProfileFragment;
import com.econnect.client.Profile.Medals.MedalUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;

public class MainActivity extends AppCompatActivity {

    private final Fragment[] _fragments = {new ProductsFragment(), new CompaniesFragment(),
            new ForumFragment(), new LoggedUserProfileFragment()};

    // Default screen is Products
    private Fragment _selectedFragment = _fragments[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnBackPressedDispatcher().addCallback(backPressedCallback);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(bottomNavSelected);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        // Add and hide all fragments, show only default fragment
        for (Fragment f : _fragments) {
            ft = ft.add(R.id.mainFrameLayout, f).hide(f);
        }
        // Default screen is Products
        ft.show(_selectedFragment).commit();
        setTitle(R.string.products_name);


        // Add callback for returning to login screen if the token expires
        Service.setInvalidTokenCallback(() -> {
            // Remove token from disk
            ServiceFactory.getInstance().getLoginService().localLogout();

            // Display error message. Block this thread until the user selects OK
            ExecutionThread.UI_blocking(_selectedFragment, ()->{
                PopupMessage.okDialog(_selectedFragment, getString(R.string.session_expired), getString(R.string.login_again), (dialog, id)->{
                    // Return to login screen
                    this.finish();
                    // User selected OK, unblock thread
                    synchronized (this) { notify(); }
                });
            });
        });

        // Add callback for showing unlocked medal popup
        Service.setMedalUnlockedCallback((medalId)->{
            ExecutionThread.UI(_selectedFragment, ()->{
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(_selectedFragment.requireContext());
                final View v = _selectedFragment.getLayoutInflater().inflate(R.layout.medal_unlocked, null);

                ImageView icon = v.findViewById(R.id.popupMedal_icon);
                TextView medalName = v.findViewById(R.id.popupMedal_name);

                icon.setImageDrawable(MedalUtils.medalIcon(medalId));
                medalName.setText(MedalUtils.medalName(medalId));

                popupBuilder.setView(v);
                final AlertDialog popupDialog = popupBuilder.create();
                popupDialog.show();
            });
        });
    }

    private final OnItemSelectedListener bottomNavSelected = item -> {
        navigateToScreen(item.getItemId());
        return true;
    };

    private void navigateToScreen(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Hide old selected fragment
        ft = ft.hide(_selectedFragment);

        if (id == R.id.products) {
            _selectedFragment = _fragments[0];
            super.setTitle(R.string.products_name);
        }
        else if (id == R.id.companies) {
            _selectedFragment = _fragments[1];
            super.setTitle(R.string.companies_name);
        }
        else if (id == R.id.forum) {
            _selectedFragment = _fragments[2];
            super.setTitle(R.string.forum_name);
        }
        else if (id == R.id.profile) {
            _selectedFragment = _fragments[3];
            super.setTitle(R.string.profile_name);
        }
        else {
            throw new RuntimeException("Invalid menu option");
        }
        // Show new selected fragment
        ft.show(_selectedFragment).commit();
        _selectedFragment.onStart();
    }

    // Double tap to exit
    private boolean _doubleBackToExitPressedOnce = false;
    private final OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            // If flag has been set, exit
            if (_doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }

            _doubleBackToExitPressedOnce = true;
            PopupMessage.showToast(MainActivity.this, getString(R.string.back_to_exit));

            // Wait 2 seconds before clearing flag
            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.postDelayed(() -> _doubleBackToExitPressedOnce=false, 2000);
        }
    };
}