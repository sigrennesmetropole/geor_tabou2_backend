package rm.tabou2.service.validator.operation.impl;

import rm.tabou2.service.mapper.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.operation.ValidOperationUpdate;

import javax.validation.ConstraintValidatorContext;

public class ValidOperationUpdateValidator implements CustomConstraintValidator<ValidOperationUpdate, OperationIntermediaire> {

    @Override
    public boolean isValid(OperationIntermediaire operation, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = operation.getId() != null && operation.getId() > 0;
        if (!idValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'id' de l'opération est invalide", "id");
        }

        boolean etapeValidation = operation.getEtape() != null;
        if (!etapeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'étape de l'opération est invalide", "etape");
        }

        return idValidation && etapeValidation;
    }
}
