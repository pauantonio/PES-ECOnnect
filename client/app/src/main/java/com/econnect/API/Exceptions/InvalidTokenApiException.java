package com.econnect.API.Exceptions;

import com.econnect.API.ApiConstants;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class InvalidTokenApiException extends ApiException {
    public InvalidTokenApiException() {
        super(ApiConstants.ERROR_INVALID_TOKEN);
    }
    
    @Override
    public String getMessage() {
        return Translate.id(R.string.session_expired_exception);
    }
}
