package com.econnect.Utilities;

import android.text.Editable;
import android.text.TextWatcher;

public class BasicTextWatcher implements TextWatcher {
    private final Runnable _runnable;
    public BasicTextWatcher(Runnable runnable) {
        _runnable = runnable;
    }
    public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
        // Do nothing
    }
    public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
        // Call runnable
        _runnable.run();
    }
    public void afterTextChanged(Editable var1) {
        // Do nothing
    }
}