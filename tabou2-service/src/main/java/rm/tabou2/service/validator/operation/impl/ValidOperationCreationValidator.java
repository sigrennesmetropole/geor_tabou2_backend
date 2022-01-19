package rm.tabou2.service.validator.operation.impl;

import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.operation.ValidOperationCreation;

import javax.validation.ConstraintValidatorContext;

public class ValidOperationCreationValidator implements CustomConstraintValidator<ValidOperationCreation, OperationIntermediaire> {

    @Override
    public boolean isValid(OperationIntermediaire operation, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        boolean etapeValidation = operation.getEtape() != null && operation.getEtape().getId() > 0;
        if (!etapeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'étape de l'opération est invalide", "etape");
        }

        boolean natureValidation = operation.getNature() != null;
        if (!natureValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La nature de l'opération est invalide", "nature");
        }

        boolean empriseValidation = operation.getIdEmprise() != null && operation.getIdEmprise() > 0;
        if (!empriseValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'identifiant de l'emprise de l'opération est invalide", "idEmprise");
        }

        boolean estSecteurValidation = operation.getSecteur() != null;
        if (!estSecteurValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La propriété 'secteur' de l'opération est invalide", "secteur");
        }

        return etapeValidation && natureValidation && empriseValidation && estSecteurValidation;
    }
}
