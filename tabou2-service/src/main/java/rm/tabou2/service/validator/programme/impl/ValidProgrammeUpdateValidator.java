package rm.tabou2.service.validator.programme.impl;

import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.programme.ValidProgrammeUpdate;

import javax.validation.ConstraintValidatorContext;

public class ValidProgrammeUpdateValidator implements CustomConstraintValidator<ValidProgrammeUpdate, Programme> {

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = programme.getId() != null && programme.getId() > 0;
        if (!idValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'id' du programme est invalide", "id");
        }

        // étape validation
        boolean etapeValidation = programme.getEtape() != null;
        if (!etapeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'étape du programme est invalide", "etape");
        }

        return idValidation && etapeValidation;
    }
}
