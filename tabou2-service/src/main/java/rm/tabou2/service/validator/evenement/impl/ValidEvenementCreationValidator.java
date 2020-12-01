package rm.tabou2.service.validator.evenement.impl;

import org.apache.commons.lang.StringUtils;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.evenement.ValidEvenementCreation;

import javax.validation.ConstraintValidatorContext;

public class ValidEvenementCreationValidator implements CustomConstraintValidator<ValidEvenementCreation, Evenement> {

    @Override
    public boolean isValid(Evenement evenement, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // idType validation
        boolean idTypeValidation = evenement.getIdType() != null && evenement.getIdType() > 0;
        if (!idTypeValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le type de l'évenement est invalide", "idType");
        }

        // eventDate validation
        boolean eventDateValidation = evenement.getEventDate() != null ;
        if (!eventDateValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'event date de l'évenement est invalide", "eventDate");
        }

        // description validation
        boolean descriptionValidation = !StringUtils.isEmpty(evenement.getDescription());
        if (!descriptionValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "La description de l'évenement est invalide", "description");
        }

        return idTypeValidation && eventDateValidation && descriptionValidation;
    }
}
