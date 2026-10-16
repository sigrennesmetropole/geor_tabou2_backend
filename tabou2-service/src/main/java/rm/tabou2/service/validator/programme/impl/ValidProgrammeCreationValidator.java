package rm.tabou2.service.validator.programme.impl;

import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.programme.ValidProgrammeCreation;

import jakarta.validation.ConstraintValidatorContext;

public class ValidProgrammeCreationValidator implements CustomConstraintValidator<ValidProgrammeCreation, Programme> {

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // etape validation
        boolean etapeValidation = programme.getEtape() != null
                && programme.getEtape().getId() != null && programme.getEtape().getId() > 0;
        if (!etapeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'étape du programme est invalide", "etape");
        }

        // operation validation
        boolean operationIdValidation = programme.getOperationId() != null;
        if (!operationIdValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'opération associée au programme est invalide'", "operationId");
        }

        // vocation validation
        boolean vocationValidation = programme.getVocation() != null && programme.getVocation().getId() != null;
        if (!vocationValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La vocation principale du programme est obligatoire", "vocation");
        }

        return etapeValidation && operationIdValidation && vocationValidation;
    }
}
