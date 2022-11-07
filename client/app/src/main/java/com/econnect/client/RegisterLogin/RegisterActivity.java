package com.econnect.client.RegisterLogin;

import android.os.Bundle;
import android.os.Looper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.econnect.Utilities.PopupMessage;
import com.econnect.client.MainActivity;
import com.econnect.client.R;
import com.econnect.client.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityRegisterBinding binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getOnBackPressedDispatcher().addCallback(backPressedCallback);

        setSupportActionBar(binding.toolbar);
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
            PopupMessage.showToast(RegisterActivity.this, getString(R.string.back_to_exit));

            // Wait 2 seconds before clearing flag
            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
            handler.postDelayed(() -> _doubleBackToExitPressedOnce=false, 2000);
        }
    };
}