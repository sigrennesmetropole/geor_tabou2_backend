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
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceExceptionsStatus;
import rm.tabou2.service.exception.AppServiceNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
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
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @ExceptionHandler({AccessDeniedException.class, AuthenticationCredentialsNotFoundException.class, RefreshTokenExpiredException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleValidationException(final Exception ex, final WebRequest request) {
        LOGGER.error("L'objet passé en paramètre contient des données invalides");
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @ExceptionHandler(AppServiceNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(final Exception ex, final WebRequest request) {
        LOGGER.error("Ressource non trouvé");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException(final Exception ex, final WebRequest request) {
        LOGGER.error("L'élement demandé n'existe pas", ex);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, final WebRequest request) {

        String error = ex.getRootCause().getMessage();

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "ConstraintViolationException : la requête ne peut être exécutée", error);

        return  new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
    }



    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception ex, final WebRequest request) {
        LOGGER.error("unknown error", ex);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
