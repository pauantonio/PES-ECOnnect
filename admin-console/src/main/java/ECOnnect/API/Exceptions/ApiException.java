package ECOnnect.API.Exceptions;

public class ApiException extends RuntimeException {
    private final String _errorCode;
    
    public ApiException(String errorCode) {
        super("The server responded with error code " + errorCode);
        _errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return _errorCode;
    }
}
