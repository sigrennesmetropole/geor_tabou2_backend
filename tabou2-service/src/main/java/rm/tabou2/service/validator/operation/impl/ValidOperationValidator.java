package rm.tabou2.service.validator.operation.impl;

import org.apache.commons.lang3.StringUtils;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.operation.ValidOperation;

import javax.validation.ConstraintValidatorContext;

public class ValidOperationValidator implements CustomConstraintValidator<ValidOperation, OperationIntermediaire> {
    @Override
    public boolean isValid(OperationIntermediaire operation, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // nom validation
        boolean nomValidation = !StringUtils.isEmpty(operation.getNom());
        if (!nomValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le nom de l'opération est invalide", "nom");
        }

        // code validation
        boolean codeValidation = !StringUtils.isEmpty(operation.getCode());
        if (!codeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le code de l'opération est invalide", "code");
        }

        return nomValidation && codeValidation;
    }
}
