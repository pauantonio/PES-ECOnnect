package com.econnect.client.RegisterLogin;

import com.econnect.Utilities.CustomFragment;
import com.econnect.client.databinding.FragmentLoginBinding;

public class LoginFragment extends CustomFragment<FragmentLoginBinding> {

    private final LoginController ctrl = new LoginController(this);

    public LoginFragment() {
        super(FragmentLoginBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.buttonLogin.setOnClickListener(ctrl.loginButton());
        binding.textLoginToRegister.setOnClickListener(ctrl.toRegisterButton());
        binding.googleLoginButton.setOnClickListener(ctrl.googleLogin());

        ctrl.attemptAutoLogin();
        ctrl.initializeThirdPartyLogins();
        ctrl.pingServer();
    }

    void enableInput(boolean enabled) {
        binding.textLoginToRegister.setEnabled(enabled);
        binding.buttonLogin.setEnabled(enabled);
        binding.googleLoginButton.setEnabled(enabled);
    }

    String getEmailText() {
        return binding.loginEmailText.getText().toString();
    }

    String getPasswordText() {
        return binding.loginPasswordText.getText().toString();
    }
}