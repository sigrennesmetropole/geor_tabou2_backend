package rm.tabou2.service.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Codes des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppServiceExceptionsStatus {

    public static final String UNAUTHORIZE = "GENIUS_ERROR_UNAUTHORIZE_401";

    public static final String FORBIDDEN = "GENIUS_ERROR_FORBIDDEN_403";

}
