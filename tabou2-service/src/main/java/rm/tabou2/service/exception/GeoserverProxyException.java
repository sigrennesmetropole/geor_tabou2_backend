package rm.tabou2.service.exception;

import lombok.Getter;

/**
 * Refus ou échec du proxy cartographique, avec le statut HTTP à renvoyer au client.
 */
@Getter
public class GeoserverProxyException extends AppServiceException {

    private static final long serialVersionUID = 1L;

    private final int httpStatus;

    public GeoserverProxyException(int httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public GeoserverProxyException(int httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
