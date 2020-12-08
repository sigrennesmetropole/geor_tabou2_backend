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

        boolean vocationValidation = operation.getVocation() != null;
        if (!vocationValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La vocation de l'opération est invalide", "vocation");
        }

        boolean decisionValidation = operation.getDecision() != null;
        if (!decisionValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La décision de l'opération est invalide", "decision");
        }

        boolean maitriseOuvrageValidation = operation.getMaitriseOuvrage() != null;
        if (!maitriseOuvrageValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La maitrise d'ouvrage de l'opération est invalide", "maitriseOuvrage");
        }

        boolean modeAmenagementValidation = operation.getModeAmenagement() != null;
        if (!modeAmenagementValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le mode d'aménagement de l'opération est invalide", "modeAmenagement");
        }

        boolean consommationEspaceValidation = operation.getConsommationEspace() != null;
        if (!consommationEspaceValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La consommation d'espace de l'opération est invalide", "consommationEspace");
        }

        return natureValidation && empriseValidation
                && vocationValidation && decisionValidation
                && maitriseOuvrageValidation && modeAmenagementValidation
                && consommationEspaceValidation;
    }
}
