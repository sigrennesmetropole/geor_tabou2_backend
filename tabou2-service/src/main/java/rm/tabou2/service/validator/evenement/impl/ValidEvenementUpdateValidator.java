package rm.tabou2.service.validator.evenement.impl;

import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.evenement.ValidEvenementUpdate;

import jakarta.validation.ConstraintValidatorContext;

public class ValidEvenementUpdateValidator implements CustomConstraintValidator<ValidEvenementUpdate, Evenement> {

    @Override
    public boolean isValid(Evenement evenement, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = evenement.getId() != null && evenement.getId() > 0;
        if (!idValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'identifiant de l'évenement est invalide", "id");
        }

        return idValidation;
    }
}
