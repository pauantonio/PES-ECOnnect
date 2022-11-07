package com.econnect.client.RegisterLogin;

import androidx.fragment.app.Fragment;

public interface IThirdPartyLogin {

    interface IThirdPartyLoginCallback {
        void onLogin(String email, String name, String token);
        void printError(String error);
    }

    void initialize(Fragment owner, IThirdPartyLoginCallback callback);
    void buttonPressed();
    void logout();
}
