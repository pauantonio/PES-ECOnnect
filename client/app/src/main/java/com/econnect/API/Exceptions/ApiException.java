package com.econnect.API.Exceptions;

import com.econnect.Utilities.Translate;
import com.econnect.client.R;

public class ApiException extends RuntimeException {
    String _errorCode;
    
    public ApiException(String errorCode) {
        super(Translate.id(R.string.server_error_code) + errorCode);
        _errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return _errorCode;
    }
}
