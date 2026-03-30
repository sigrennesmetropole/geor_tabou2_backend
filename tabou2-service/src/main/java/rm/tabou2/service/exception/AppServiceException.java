package rm.tabou2.service.exception;

import lombok.Getter;

@Getter
public class AppServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    protected final String appExceptionStatusCode;


    public AppServiceException() {
        super();
        appExceptionStatusCode = AppServiceExceptionsStatus.INTERNAL_ERROR;
    }

    public AppServiceException(final String message) {
        super(message);
        appExceptionStatusCode = AppServiceExceptionsStatus.INTERNAL_ERROR;
    }

    public AppServiceException(final String message, final String exceptionStatusCode) {
        super(message);
        appExceptionStatusCode = exceptionStatusCode;
    }

    public AppServiceException(final String message, final Throwable exception) {
        super(message, exception);
        appExceptionStatusCode = AppServiceExceptionsStatus.INTERNAL_ERROR;
    }

    public AppServiceException(final String message, final Throwable exception, final String exceptionStatusCode) {
        super(message, exception);
        appExceptionStatusCode = exceptionStatusCode;
    }
}
