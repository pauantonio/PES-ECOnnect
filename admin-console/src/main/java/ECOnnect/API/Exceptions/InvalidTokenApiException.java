package ECOnnect.API.Exceptions;

import ECOnnect.API.ApiConstants;

public class InvalidTokenApiException extends ApiException {
    public InvalidTokenApiException() {
        super(ApiConstants.ERROR_INVALID_TOKEN);
    }
    
    @Override
    public String getMessage() {
        return "This session has expired, please logout and try again";
    }
}
