package rm.tabou2.service.exception;

public class AppServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    protected String appExceptionStatusCode;


    public AppServiceException() {
        super();
    }

    public AppServiceException(final String message) {
        super(message);
    }

    public AppServiceException(final String message, final String exceptionStatusCode) {
        super(message);
        this.appExceptionStatusCode = exceptionStatusCode;
    }

    public AppServiceException(final String message, final Throwable exception) {
        super(message, exception);
    }

    public AppServiceException(final String message, final Throwable exception, final String exceptionStatusCode) {
        super(message, exception);
        this.appExceptionStatusCode = exceptionStatusCode;
    }


    public String getAppExceptionStatusCode() {
        return appExceptionStatusCode;
    }

    public void setAppExceptionStatusCode(final String AppExceptionStatusCode) {
        this.appExceptionStatusCode = AppExceptionStatusCode;
    }
}
