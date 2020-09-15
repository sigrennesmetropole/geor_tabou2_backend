package rm.tabou2.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    private static final Integer DEFAULT_START = 0;
    private static final Integer DEFAULT_RESULTS_NUMBER = 10;

    /**
     * Constructeur de la classe utilitaire
     */
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static Pageable buildPageable(Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        if (null == start) {
            start = DEFAULT_START;
        }
        if (null == resultsNumber) {
            resultsNumber = DEFAULT_RESULTS_NUMBER;
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if (Boolean.FALSE.equals(asc)) {
            direction = Sort.Direction.DESC;
        }

        return PageRequest.of(start, resultsNumber, Sort.by(direction, orderBy));

    }


    /**
     * Retourne le nom de l'utilisateur connecté.
     *
     * @return username
     */
    public static String getConnectedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
