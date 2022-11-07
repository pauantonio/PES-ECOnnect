package com.econnect.client.RegisterLogin;

import android.view.View;

import com.econnect.API.RegisterService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.SettingsFile;
import com.econnect.client.R;

public class RegisterController {
    private final RegisterFragment _fragment;

    RegisterController(RegisterFragment fragment) {
        this._fragment = fragment;
    }

    // Boilerplate for interfacing with the fragment
    View.OnClickListener registerButton() { return view -> registerButtonClick(); }
    View.OnClickListener toLoginButton() { return view -> loginButtonClick(); }

    private void registerButtonClick() {
        // Get text fields
        String user_name = _fragment.getUsernameText();
        String user_pass = _fragment.getPasswordText();
        String user_email = _fragment.getEmailText();

        // Local validation
        if (user_email.isEmpty() || user_name.isEmpty() || user_pass.isEmpty()) {
            PopupMessage.warning(_fragment, _fragment.getString(R.string.must_fill_all_fields));
            return;
        }
        _fragment.enableInput(false);
        ExecutionThread.nonUI(() -> {
            try{
                RegisterService registerService = ServiceFactory.getInstance().getRegisterService();
                SettingsFile file = new SettingsFile(_fragment);
                registerService.register(user_email, user_pass, user_name, file);
                ExecutionThread.UI(_fragment, ()->{
                    _fragment.enableInput(true);
                    ExecutionThread.navigate(_fragment, R.id.action_successful_register);
                });

            } catch (Exception e){
                ExecutionThread.UI(_fragment, ()->{
                    _fragment.enableInput(true);
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_signup) + e.getMessage());
                });
            }

        });

    }

    private void loginButtonClick() {
        ExecutionThread.navigateUp(_fragment);
    }
}
