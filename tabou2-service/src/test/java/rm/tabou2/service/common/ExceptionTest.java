package rm.tabou2.service.common;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

public interface ExceptionTest {

    /**
     * Test pour s'assurer que la contrainte pour effectivement sur les propriétés attendues
     * @param constraintViolationException  exception
     * @param listProperty                  liste des propriétés
     */
    default void testConstraintViolationException(ConstraintViolationException constraintViolationException, List<String> listProperty) {
        Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();

        Assertions.assertEquals(listProperty.size(), constraintViolations.size());

        List<String> errorProperties = new ArrayList<>();
        constraintViolations.forEach(cv -> {
            String name = StreamSupport.stream(cv.getPropertyPath().spliterator(), false)
                    .map(Path.Node::getName)
                    .reduce((first, second) -> second).orElseGet(() -> cv.getPropertyPath().toString());
            errorProperties.add(name);
        });

        Assertions.assertTrue(CollectionUtils.isEqualCollection(errorProperties, listProperty));
    }
}
