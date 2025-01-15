package rm.tabou2.service.helper.plh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;
import rm.tabou2.storage.tabou.entity.plh.TypeAttributPLH;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.List;
import java.util.Set;

@Component
public class TypePlhHelper {

    @Autowired
    private TypePLHDao typePlhDao;

    /**
     * Peuple de façon récursive un type PLH avec ses valeurs associées dans le programme
     * @param typePLH   type PLH à peupler
     * @param programmeEntity le programmeEntity duquel est rattaché le typePLH
     * @return le typePLH peuplé
     */
    public TypePLH populateTypePlh (TypePLH typePLH, ProgrammeEntity programmeEntity) {
        // Si c'est une catégorie
        if (typePLH.getTypeAttributPLH() == TypePLH.TypeAttributPLHEnum.CATEGORY) {
            // et qu'elle a des fils
            if (!CollectionUtils.isEmpty(typePLH.getFils())) {
                List<TypePLH> fils = typePLH.getFils();
                // Alors, on réutilise récursivement populateTypePlh pour voir si les fils sont à peupler
                for (TypePLH typePLHFils : fils) {
                    populateTypePlh(typePLHFils, programmeEntity);
                }
            }

        }
        // sinon, c'est donc un type VALUE
        else {
            // on récupère les attributs du programme
            if (!CollectionUtils.isEmpty(programmeEntity.getAttributsPLH())) {
                Set<AttributPLHEntity> attributPLHEntities = programmeEntity.getAttributsPLH();
                // on fait setValue() si on trouve un attribut correspondant à notre type PLH
                for (AttributPLHEntity attributPLHEntity : attributPLHEntities) {
                    if (attributPLHEntity.getType().getId() == typePLH.getId()) {
                        typePLH.setValue(attributPLHEntity.getValue());
                    }
                }
            }
        }
        return typePLH;
    }

    /**
     * Vérification récursive qu'un TypePLH et ses fils ne possède pas de fils s'ils sont du type attribut VALUE
     * @param typePLH   type PLH à vérifier
     */
    public void checkAucunAttributVALUEaUnFils (TypePLHEntity typePLH) throws AppServiceException {
        if (!CollectionUtils.isEmpty(typePLH.getFils())) {
            if (typePLH.getTypeAttributPLH().equals(TypeAttributPLH.VALUE)) {
                throw new AppServiceException("Le type PLH id = " + typePLH.getId() +
                        " est une VALUE et ne peut pas avoir de fils.");
            } else {
                Set<TypePLHEntity> fils = typePLH.getFils();
                for (TypePLHEntity typePLHEntity : fils) {
                    checkAucunAttributVALUEaUnFils(typePLHEntity);
                }
            }
        }
    }

    /**
     * Met à jour de façon récursive toutes les valeurs qui auraient changé dans l'arborescence du type PLH
     * @param typePLH   type PLH à mettre à jour
     * @param programmeEntity le programmeEntity duquel est rattaché le typePLH
     * @return le typePLH mis à jour
     */
    public TypePLH updateValuesTypePlh (TypePLH typePLH, ProgrammeEntity programmeEntity) {
        // Si c'est une catégorie
        if (typePLH.getTypeAttributPLH() == TypePLH.TypeAttributPLHEnum.CATEGORY) {
            // et qu'elle a des fils
            if (!CollectionUtils.isEmpty(typePLH.getFils())) {
                List<TypePLH> fils = typePLH.getFils();
                // Alors, on réutilise récursivement updateValuesTypePlh pour voir si les fils sont à mettre à jour
                for (TypePLH typePLHFils : fils) {
                    updateValuesTypePlh(typePLHFils, programmeEntity);
                }
            }

        }
        // sinon, c'est donc un type VALUE
        else {
            // on récupère les attributs du programme
            Set<AttributPLHEntity> attributsPLH = programmeEntity.getAttributsPLH();
            if (!CollectionUtils.isEmpty(attributsPLH)) {
                // on fait setValue() si on trouve un attribut correspondant à notre type PLH
                for (AttributPLHEntity attributPLHEntity : attributsPLH) {
                    if (attributPLHEntity.getType().getId() == typePLH.getId()) {
                        attributPLHEntity.setValue(typePLH.getValue());
                        return typePLH;
                    }
                }
            }
            // sinon, on la créé et on l'ajoute
            AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
            attributPLHEntity.setValue(typePLH.getValue());
            attributPLHEntity.setType(typePlhDao.findOneById(typePLH.getId()));
            programmeEntity.addAttributPLHProgramme(attributPLHEntity);
        }
        return typePLH;
    }

}
