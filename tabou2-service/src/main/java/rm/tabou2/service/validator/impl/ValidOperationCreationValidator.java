package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.validator.ValidOperationCreation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidOperationCreationValidator implements ConstraintValidator<ValidOperationCreation, Operation> {

    @Override
    public boolean isValid(Operation operation, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

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

        boolean natureValidation = operation.getNature() != null;
        if (!natureValidation) {
            customMessageForValidation(constraintValidatorContext, "La nature de l'opération est invalide", "nature");
        }

        boolean empriseValidation = operation.getIdEmprise() != null && operation.getIdEmprise() > 0;
        if (!empriseValidation) {
            customMessageForValidation(constraintValidatorContext, "L'identifiant de l'emprise de l'opération est invalide", "idEmprise");
        }

        return nomValidation && codeValidation && natureValidation && empriseValidation;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message, String property) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
    }
}
