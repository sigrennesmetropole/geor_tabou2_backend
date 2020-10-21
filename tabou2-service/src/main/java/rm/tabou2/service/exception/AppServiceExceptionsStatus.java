package rm.tabou2.service.exception;

/**
 * Codes des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
public class AppServiceExceptionsStatus {

    // Constructeur privé par défaut
    private AppServiceExceptionsStatus() {
    }

    //you aren’t authenticated–either not authenticated at all or authenticated incorrectly–but please reauthenticate and try again.
    public static final String UNAUTHORIZE = "GENIUS_ERROR_UNAUTHORIZE_401";

    //“I’m sorry. I know who you are–I believe who you say you are–but you just don’t have permission to access this resource.
    public static final String FORBIDDEN = "GENIUS_ERROR_FORBIDDEN_403";

}
