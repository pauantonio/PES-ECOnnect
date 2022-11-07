package com.econnect.API.Exceptions;

import com.econnect.API.ApiConstants;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class ProfileIsPrivateException extends ApiException {

    public ProfileIsPrivateException() {
        super(ApiConstants.ERROR_PRIVATE_USER);
    }

    @Override
    public String getMessage() {
        return Translate.id(R.string.profile_is_private);
    }
}
