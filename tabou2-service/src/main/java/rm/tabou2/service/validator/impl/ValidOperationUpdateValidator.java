package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.validator.ValidOperationUpdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOperationUpdateValidator implements ConstraintValidator<ValidOperationUpdate, Operation> {

    @Override
    public boolean isValid(Operation operation, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = operation.getId() > 0;
        if (!idValidation) {
            customMessageForValidation(constraintValidatorContext, "L'id' de l'opération est invalide", "id");
        }

        // nom validation
        boolean nomValidation = !StringUtils.isEmpty(operation.getNom());
        if (!nomValidation) {
            customMessageForValidation(constraintValidatorContext, "Le nom de l'opération est invalide", "nom");
        }

        // code validation
        boolean codeValidation = !StringUtils.isEmpty(operation.getCode()) ;
        if (!codeValidation) {
            customMessageForValidation(constraintValidatorContext, "Le code de l'opération est invalide", "code");
        }

        boolean etapeValidation = operation.getEtape() != null;
        if (!etapeValidation) {
            customMessageForValidation(constraintValidatorContext, "L'étape de l'opération est invalide", "etape");
        }

        return idValidation && nomValidation && codeValidation && etapeValidation;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message, String property) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
    }
}
