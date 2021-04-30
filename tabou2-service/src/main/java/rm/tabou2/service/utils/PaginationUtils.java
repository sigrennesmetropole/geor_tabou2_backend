package rm.tabou2.service.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import rm.tabou2.service.dto.PageResult;

import java.lang.reflect.Field;
import java.util.ArrayList;

@Configuration
public class PaginationUtils {

    private static final Integer DEFAULT_START = 0;

    @Value("${pagination.default.max.results}")
    private int maxResults;

    private static int maxResultsStatic;

    @Value("${pagination.default.max.results}")
    public void setMaxResults(int name) {
        this.maxResultsStatic = name;
    }


    public static Pageable buildPageable(Integer start, Integer resultsNumber, String orderBy, Boolean asc, Class<?> classname) {

        if (null == start) {
            start = DEFAULT_START;
        }
        if (null == resultsNumber) {
            resultsNumber = maxResultsStatic;
        }

        Sort.Direction direction = Sort.Direction.ASC;
        if (Boolean.FALSE.equals(asc) || null == asc) {
            direction = Sort.Direction.DESC;
        }

        if (null == orderBy) {
            Field[] classFields = classname.getDeclaredFields();
            for (Field f : classFields) {
                if (f.isAnnotationPresent(javax.persistence.OrderBy.class)) {
                    orderBy = f.getName();
                }
            }
        }

        return PageRequest.of(start, resultsNumber, Sort.by(direction, orderBy));

    }

    public static PageResult buildPageResult(Page<?> page) {

        PageResult result = new PageResult();

        result.setTotalElements(page.getTotalElements());
        result.setElements(new ArrayList<>());
        result.getElements().addAll(page.getContent());

        return result;

    }


}
