package com.econnect.API.Exceptions;

import com.econnect.API.ApiConstants;
import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class AccountNotFoundException extends ApiException {

    public AccountNotFoundException() {
        super(ApiConstants.ERROR_USER_NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return Translate.id(R.string.no_account_found);
    }
}
