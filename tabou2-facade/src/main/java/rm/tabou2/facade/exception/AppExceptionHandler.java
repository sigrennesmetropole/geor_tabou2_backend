package rm.tabou2.facade.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.exception.AppServiceNotFoundException;

import jakarta.validation.ConstraintViolationException;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class AppExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(rm.tabou2.service.exception.AppServiceException.class)
    protected ResponseEntity<Object> handleExceptionService(final AppServiceException ex, final WebRequest request) {

        LOGGER.error(ex.getMessage(), ex);

        if (ex.getAppExceptionStatusCode() != null) {

            if (ex.getAppExceptionStatusCode().equals(AppServiceExceptionsStatus.UNAUTHORIZE)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } else if (ex.getAppExceptionStatusCode().equals(AppServiceExceptionsStatus.FORBIDDEN)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else if(ex.getAppExceptionStatusCode().equals(AppServiceExceptionsStatus.BADREQUEST)) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class, RefreshTokenExpiredException.class})
    protected ResponseEntity<Object> handleUnauthorizedException(final Exception ex, final WebRequest request) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler({ConstraintViolationException.class, InvalidParameterException.class})
    protected ResponseEntity<Object> handleValidationException(final Exception ex, final WebRequest request) {
        LOGGER.error("L'objet passé en paramètre contient des données invalides");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(AppServiceNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        LOGGER.error("Ressource non trouvé");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(final Exception ex, final WebRequest request) {
        LOGGER.error("L'élement demandé n'existe pas", ex);
        return new ResponseEntity<>("L'élement demandé n'existe pas", HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, final WebRequest request) {
        String error = "";
        Throwable rootCause = ex.getRootCause();
        if(rootCause != null){
            error = rootCause.getMessage();
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "ConstraintViolationException : la requête ne peut être exécutée", error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleFileSizeLimitExceededException(final MaxUploadSizeExceededException ex, final WebRequest request){
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>("Le fichier fourni est trop volumineux", HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(final MultipartException ex, final WebRequest request){
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>("La requête est trop volumineuse", HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception ex, final WebRequest request) {
        LOGGER.error("unknown error", ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
