package rm.tabou2.service.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Codes des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppServiceExceptionsStatus {

    // Erreurs 4XX
    public static final String BADREQUEST = "GENIUS_ERROR_BADREQUEST_400";

    public static final String UNAUTHORIZE = "GENIUS_ERROR_UNAUTHORIZE_401";

    public static final String FORBIDDEN = "GENIUS_ERROR_FORBIDDEN_403";

    public static final String NOT_FOUND = "GENIUS_ERROR_NOT_FOUND_404";

    //Erreurs 5XX
    public static final String INTERNAL_ERROR = "GENIUS_ERROR_INTERNAL_500";
}
