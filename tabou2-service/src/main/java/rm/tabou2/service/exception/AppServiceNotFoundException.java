package rm.tabou2.service.exception;

public class AppServiceNotFoundException extends AppServiceException {
	public AppServiceNotFoundException(Class<?> clazz) {
		super(clazz.getSimpleName() + " not found", AppServiceExceptionsStatus.NOT_FOUND);
	}

	public AppServiceNotFoundException(String message) {
		super(message, AppServiceExceptionsStatus.NOT_FOUND);
	}
}
