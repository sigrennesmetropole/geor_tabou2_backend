package rm.tabou2.service.validator.programme.impl;

import org.apache.commons.lang3.StringUtils;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.programme.ValidProgramme;

import javax.validation.ConstraintValidatorContext;

public class ValidProgrammeValidator implements CustomConstraintValidator<ValidProgramme, Programme> {
    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // nom validation
        boolean nomValidation = !StringUtils.isEmpty(programme.getNom());
        if (!nomValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le nom du programme est invalide", "nom");
        }

        // code validation
        boolean codeValidation = !StringUtils.isEmpty(programme.getCode()) ;
        if (!codeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le code du programme est invalide", "code");
        }

        return nomValidation && codeValidation;
    }
}
