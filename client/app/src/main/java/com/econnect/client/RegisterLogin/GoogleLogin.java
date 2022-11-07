package com.econnect.client.RegisterLogin;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.fragment.app.Fragment;

import com.econnect.Utilities.Translate;
import com.econnect.client.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleLogin implements IThirdPartyLogin {

    private GoogleSignInClient _loginClient;
    private ActivityResultLauncher<Intent> _activityLauncher;
    private IThirdPartyLoginCallback _callback;

    @Override
    public void initialize(Fragment owner, IThirdPartyLoginCallback callback) {
        _callback = callback;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(owner.getString(R.string.google_client_id))
                .requestEmail()
                .build();
        _loginClient = GoogleSignIn.getClient(owner.requireContext(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(owner.requireContext());
        if (account != null) {
            _callback.onLogin(account.getEmail(), account.getDisplayName(), account.getIdToken());
        }

        _activityLauncher = owner.registerForActivityResult(new StartActivityForResult(), this::loginActivityCallback);
    }

    @Override
    public void buttonPressed() {
        _activityLauncher.launch(_loginClient.getSignInIntent());
    }

    @Override
    public void logout() {
        _loginClient.signOut();
    }

    private void loginActivityCallback(ActivityResult result) {
        // Called once the user selects a google account
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            _callback.onLogin(account.getEmail(), account.getDisplayName(), account.getId());
            // Logout from Google (keep ECOnnect token) so that we can choose a different account next time
            logout();
        }
        catch (ApiException e) {
            // There has been an error, maybe user has cancelled login. Do nothing
            _callback.printError(Translate.id(R.string.google_login_error));
        }
    }
}
