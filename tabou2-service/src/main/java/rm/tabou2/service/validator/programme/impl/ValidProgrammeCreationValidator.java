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
        boolean operationIdValidation = programme.getOperationId() != null;
        if (!operationIdValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'opération associée au programme est invalide'", "operationId");
        }

        return operationIdValidation;
    }
}
