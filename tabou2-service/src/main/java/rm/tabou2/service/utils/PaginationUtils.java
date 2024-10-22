package rm.tabou2.service.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import rm.tabou2.service.dto.PageResult;

@Configuration
public class PaginationUtils {

    private static final Integer DEFAULT_START = 0;
    public static final String ALFRESCO_DEFAULT_SORT_BY_ID = "id";

    private static int maxResultsStatic;

    @Value("${pagination.default.max.results}")
    public void setMaxResults(int name) {
        maxResultsStatic = name;
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
                if (f.isAnnotationPresent(jakarta.persistence.OrderBy.class)) {
                    orderBy = f.getName();
                }
            }
        }

        return PageRequest.of(start, resultsNumber, Sort.by(direction, orderBy));

    }

    /**
     * Construiction de la pagination pour Alfresco.
     *
     * @param start numéro du premier élément à retourner
     * @param resultsNumber nombre de résulats par page
     * @param orderBy colonne de tri
     * @param asc true si ascendant, false sino
     * @return
     */
    public static Pageable buildPageableForAlfresco(Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

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
           //Par défaut, on tri par id
            orderBy = ALFRESCO_DEFAULT_SORT_BY_ID;
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
