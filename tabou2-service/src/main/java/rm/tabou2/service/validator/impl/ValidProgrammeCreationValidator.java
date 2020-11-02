package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.ValidProgrammeCreation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidProgrammeCreationValidator implements ConstraintValidator<ValidProgrammeCreation, Programme> {

    @Override
    public void initialize(ValidProgrammeCreation constraintAnnotation) {
        //nothing to do
    }

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

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

        return nomValidation && codeValidation;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
