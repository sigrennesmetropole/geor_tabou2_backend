package rm.tabou2.service.validator.operation.impl;

import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.operation.ValidOperationCreation;

import javax.validation.ConstraintValidatorContext;

public class ValidOperationCreationValidator implements CustomConstraintValidator<ValidOperationCreation, Operation> {

    @Override
    public boolean isValid(Operation operation, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        boolean natureValidation = operation.getNature() != null;
        if (!natureValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La nature de l'opération est invalide", "nature");
        }

        boolean empriseValidation = operation.getIdEmprise() != null && operation.getIdEmprise() > 0;
        if (!empriseValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'identifiant de l'emprise de l'opération est invalide", "idEmprise");
        }

        return natureValidation && empriseValidation;
    }
}
