package com.econnect.client.RegisterLogin;

import com.econnect.Utilities.CustomFragment;
import com.econnect.client.databinding.FragmentRegisterBinding;

public class RegisterFragment extends CustomFragment<FragmentRegisterBinding> {

    private final RegisterController ctrl = new RegisterController(this);

    public RegisterFragment() {
        super(FragmentRegisterBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.buttonRegister.setOnClickListener(ctrl.registerButton());
        binding.textRegisterToLogin.setOnClickListener(ctrl.toLoginButton());
    }


    String getUsernameText() {
        return binding.registerUsernameText.getText().toString();
    }

    String getPasswordText() {
        return binding.registerPasswordText.getText().toString();
    }

    String getEmailText() {
        return binding.registerEmailText.getText().toString();
    }

    public void enableInput(boolean b) {
        binding.buttonRegister.setEnabled(b);
        binding.textRegisterToLogin.setEnabled(b);
    }
}