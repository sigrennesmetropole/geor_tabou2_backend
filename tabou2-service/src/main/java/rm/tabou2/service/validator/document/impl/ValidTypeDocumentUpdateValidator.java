package rm.tabou2.service.validator.document.impl;

import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.document.ValidTypeDocumentUpdate;

import jakarta.validation.ConstraintValidatorContext;

public class ValidTypeDocumentUpdateValidator implements CustomConstraintValidator<ValidTypeDocumentUpdate, TypeDocument> {

    @Override
    public boolean isValid(TypeDocument typeDocument, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = typeDocument.getId() != null && typeDocument.getId() > 0;
        if (!idValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "L'id' du programme est invalide", "id");
        }

        return idValidation;
    }
}
