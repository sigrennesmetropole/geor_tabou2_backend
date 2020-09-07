package rm.tabou2.service.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;

public class Utils {

    private static Integer DEFAULT_START = 0;
    private static Integer DEFAULT_RESULTS_NUMBER = 10;


    public static Pageable buildPageable(Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        if (null == start) {
            start = DEFAULT_START;
        }
        if (null == resultsNumber) {
            resultsNumber = DEFAULT_RESULTS_NUMBER;
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if (!asc) {
            direction = Sort.Direction.DESC;
        }

       return PageRequest.of(start, resultsNumber, Sort.by(direction, orderBy));

    }

}
