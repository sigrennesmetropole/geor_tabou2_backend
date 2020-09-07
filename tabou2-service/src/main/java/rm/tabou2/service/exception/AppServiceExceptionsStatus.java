package rm.tabou2.service.exception;

/**
 * Codes des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
public class AppServiceExceptionsStatus {

    // Constructeur privé par défaut
    private AppServiceExceptionsStatus() {
    }

    public static final String UNAUTHORIZE = "GENIUS_ERROR_UNAUTHORIZE_401";

    public static final String FORBIDDEN = "GENIUS_ERROR_FORBIDDEN_403";

}
