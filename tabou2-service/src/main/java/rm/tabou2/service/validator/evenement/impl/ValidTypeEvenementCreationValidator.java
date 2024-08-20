package rm.tabou2.service.validator.evenement.impl;

import org.apache.commons.lang3.StringUtils;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.evenement.ValidTypeEvenementCreation;

import javax.validation.ConstraintValidatorContext;

public class ValidTypeEvenementCreationValidator implements CustomConstraintValidator<ValidTypeEvenementCreation, TypeEvenement> {

    @Override
    public boolean isValid(TypeEvenement typeEvenement, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // libelle validation
        boolean libelleValidation = !StringUtils.isEmpty(typeEvenement.getLibelle());
        if (!libelleValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le libelle du type d'événement est invalide", "libelle");
        }

        // libelle validation
        boolean codeValidation = !StringUtils.isEmpty(typeEvenement.getCode());
        if (!codeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le code du type d'événement est invalide", "code");
        }

        return libelleValidation && codeValidation;
    }
}
