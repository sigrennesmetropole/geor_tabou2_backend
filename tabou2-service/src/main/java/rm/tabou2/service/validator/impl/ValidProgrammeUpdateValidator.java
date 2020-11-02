package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.ValidProgrammeUpdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidProgrammeUpdateValidator implements ConstraintValidator<ValidProgrammeUpdate, Programme> {

    @Override
    public void initialize(ValidProgrammeUpdate constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = programme.getId() > 0;
        if (!idValidation) {
            customMessageForValidation(constraintValidatorContext, "L'id' du programme est invalide");
        }

        // nom validation
        boolean nomValidation = !StringUtils.isEmpty(programme.getNom());
        if (!nomValidation) {
            customMessageForValidation(constraintValidatorContext, "Le nom du programme est invalide");
        }

        // code validation
        boolean codeValidation = !StringUtils.isEmpty(programme.getCode()) ;
        if (!codeValidation) {
            customMessageForValidation(constraintValidatorContext, "Le code du programme est invalide");
        }

        return idValidation && nomValidation && codeValidation;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
