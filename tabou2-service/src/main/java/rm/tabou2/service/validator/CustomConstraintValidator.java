package rm.tabou2.service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public interface CustomConstraintValidator<A extends Annotation, T> extends ConstraintValidator<A , T> {

    @Override
    default void initialize(A constraintAnnotation) {
    }

    boolean isValid(T t, ConstraintValidatorContext constraintValidatorContext);

    /**
     * Ajouter un message et la propriété erreur dans le contexte de la contrainte
     * @param constraintContext     contexte
     * @param message               message d'erreur
     * @param property              propriété concernée
     */
    default void addConstraintErrorProperty(ConstraintValidatorContext constraintContext, String message, String property) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
    }
}
