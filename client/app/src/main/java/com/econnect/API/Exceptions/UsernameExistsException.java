package com.econnect.API.Exceptions;

import com.econnect.API.ApiConstants;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class UsernameExistsException extends ApiException {

    public UsernameExistsException() {
        super(ApiConstants.ERROR_USERNAME_EXISTS);
    }

    @Override
    public String getMessage() {
        return Translate.id(R.string.username_taken);
    }
}
