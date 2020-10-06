package rm.tabou2.service.validator.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.EtapeProgrammeWorkflowHelper;
import rm.tabou2.service.validator.ValidProgrammeUpdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidProgrammeUpdateValidator implements ConstraintValidator<ValidProgrammeUpdate, Programme> {

    @Autowired
    EtapeProgrammeWorkflowHelper etapeProgrammeWorkflowHelper;

    @Override
    public void initialize(ValidProgrammeUpdate constraintAnnotation) {
        // nothing to do
    }

    @Override
    public boolean isValid(Programme programme, ConstraintValidatorContext constraintValidatorContext) {

        // désactivation du message par défaut
        constraintValidatorContext.disableDefaultConstraintViolation();

        // id validation
        boolean idValidation = programme.getId() > 0;
        if (!idValidation) {
            customMessageForValidation(constraintValidatorContext, "L'id' du programme est invalide");
            return false;
        }

        // nom validation
        boolean nomValidation = !StringUtils.isEmpty(programme.getNom());
        if (!nomValidation) {
            customMessageForValidation(constraintValidatorContext, "Le nom du programme est invalide");
            return false;
        }

        // code validation
        boolean codeValidation = !StringUtils.isEmpty(programme.getCode()) ;
        if (!codeValidation) {
            customMessageForValidation(constraintValidatorContext, "Le code du programme est invalide");
            return false;
        }

        // validation de l'étape
        boolean etapeValidation = etapeProgrammeWorkflowHelper.checkCanAssignEtapeToProgramme(programme.getEtape(), programme.getId());
        if (!etapeValidation) {
            customMessageForValidation(constraintValidatorContext, "L'étape ne peut être assigné au programme'");
            return false;
        }

        // diffusion restreinte
        boolean diffusionRestreinteValidation = programme.isDiffusionRestreinte() != null;
        if (!diffusionRestreinteValidation) {
            customMessageForValidation(constraintValidatorContext, "La diffusion restreinte du programme est invalide");
            return false;
        }

        return true;
    }

    private void customMessageForValidation(ConstraintValidatorContext constraintContext, String message) {
        // build new violation message and add it
        constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
