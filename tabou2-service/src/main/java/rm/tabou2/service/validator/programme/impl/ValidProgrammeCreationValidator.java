package rm.tabou2.service.validator.programme.impl;

import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.programme.ValidProgrammeCreation;

import javax.validation.ConstraintValidatorContext;

public class ValidProgrammeCreationValidator implements CustomConstraintValidator<ValidProgrammeCreation, Programme> {

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // operation validation
        boolean operationValidation = programme.getOperation() != null;
        if (!operationValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'opération associée au programme est invalide'", "operation");
        }

        return operationValidation;
    }
}
