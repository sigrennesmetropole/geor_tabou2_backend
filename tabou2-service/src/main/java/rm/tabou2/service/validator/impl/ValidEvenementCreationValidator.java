package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.validator.ValidEvenementCreation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidEvenementCreationValidator implements ConstraintValidator<ValidEvenementCreation, Evenement> {

    @Override
    public boolean isValid(Evenement evenement, ConstraintValidatorContext constraintValidatorContext) {
        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // idType validation
        boolean idTypeValidation = evenement.getIdType() != null && evenement.getIdType() > 0;
        if (!idTypeValidation) {
            customMessageForValidation(constraintValidatorContext, "Le type de l'évenement est invalide", "idType");
        }

        // eventDate validation
        boolean eventDateValidation = evenement.getEventDate() != null ;
        if (!eventDateValidation) {
            customMessageForValidation(constraintValidatorContext, "L'event date de l'évenement est invalide", "eventDate");
        }

        // description validation
        boolean descriptionValidation = !StringUtils.isEmpty(evenement.getDescription());
        if (!descriptionValidation) {
            customMessageForValidation(constraintValidatorContext, "La description de l'évenement est invalide", "description");
        }

        return idTypeValidation && eventDateValidation && descriptionValidation;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message, String property) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addPropertyNode(property).addConstraintViolation();
    }
}
