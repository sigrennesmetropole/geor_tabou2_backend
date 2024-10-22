package rm.tabou2.service.validator.document.impl;

import org.apache.commons.lang3.StringUtils;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.validator.CustomConstraintValidator;
import rm.tabou2.service.validator.document.ValidTypeDocumentCreation;

import jakarta.validation.ConstraintValidatorContext;

public class ValidTypeDocumentCreationValidator implements CustomConstraintValidator<ValidTypeDocumentCreation, TypeDocument> {

    @Override
    public boolean isValid(TypeDocument typeDocument, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // libelle validation
        boolean libelleValidation = !StringUtils.isEmpty(typeDocument.getLibelle());
        if (!libelleValidation) {
            addConstraintErrorProperty(constraintValidatorContext, "Le libelle du type de document est invalide", "libelle");
        }

        return libelleValidation;
    }
}
