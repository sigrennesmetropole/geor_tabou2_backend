package rm.tabou2.service.helper.plh;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.storage.tabou.dao.plh.AttributPLHDao;
import rm.tabou2.storage.tabou.dao.plh.TypePLHDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;
import rm.tabou2.storage.tabou.entity.plh.TypeAttributPLH;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@RequiredArgsConstructor
@Component
public class TypePlhHelper {

    private final TypePLHDao typePlhDao;

    private final AttributPLHDao attributPLHDao;

    /**
     * Récupère un type PLH par son identifiant.
     *
     * @param typePLHId identifiant du type PLH
     * @return l'entité type PLH
     * @throws AppServiceNotFoundException si le type PLH n'existe pas
     */
    public TypePLHEntity getTypePLHEntity(long typePLHId) throws AppServiceException {
        return typePlhDao.findById(typePLHId)
                .orElseThrow(() -> new AppServiceNotFoundException(TypePLHEntity.class));
    }

    /**
     * Peuple de façon récursive un type PLH avec ses valeurs associées dans le
     * programme
     *
     * @param typePLH         type PLH à peupler
     * @param programmeEntity le programmeEntity duquel est rattaché le typePLH
     * @return le typePLH peuplé
     */
    public TypePLH populateTypePLH(TypePLH typePLH, ProgrammeEntity programmeEntity) {
        return populateTypePLH(typePLH, programmeEntity == null ? null : programmeEntity.getAttributsPLH());
    }

    /**
     * Peuple de façon récursive un type PLH avec ses valeurs associées dans
     * l'opération
     *
     * @param typePLH         type PLH à peupler
     * @param operationEntity le operationEntity duquel est rattaché le typePLH
     * @return le typePLH peuplé
     */
    public TypePLH populateTypePLH(TypePLH typePLH, OperationEntity operationEntity) {
        return populateTypePLH(typePLH, operationEntity == null ? null : operationEntity.getAttributsPLH());
    }

    private TypePLH populateTypePLH(TypePLH typePLH, Set<AttributPLHEntity> attributsPLH) {
        if (typePLH == null) {
            return null;
        }
        if (typePLH.getTypeAttributPLH() == TypePLH.TypeAttributPLHEnum.CATEGORY) {
            populateTypeCategoryPLH(typePLH, attributsPLH);
        } else {
            populateTypeValuePLH(typePLH, attributsPLH);
        }
        return typePLH;
    }

    private void populateTypeCategoryPLH(TypePLH typePLH, Set<AttributPLHEntity> attributsPLH) {
        if (CollectionUtils.isNotEmpty(typePLH.getFils())) {
            // Alors, on réutilise récursivement populateTypePlh pour voir si les fils sont
            // à peupler
            for (TypePLH typePLHFils : typePLH.getFils()) {
                if (typePLHFils != null) {
                    populateTypePLH(typePLHFils, attributsPLH);
                }
            }
        }
    }

    private void populateTypeValuePLH(TypePLH typePLH, Set<AttributPLHEntity> attributsPLH) {
        if (CollectionUtils.isEmpty(attributsPLH)) {
            return;
        }
        for (AttributPLHEntity attributPLHEntity : attributsPLH) {
            if (attributPLHEntity != null && attributPLHEntity.getType() != null &&
                    Objects.equals(attributPLHEntity.getType().getId(), typePLH.getId())) {
                typePLH.setValue(attributPLHEntity.getValue());
            }
        }
    }

    /**
     * Vérification récursive qu'un TypePLH et ses fils ne possède pas de fils s'ils
     * sont du type attribut VALUE
     *
     * @param typePLH type PLH à vérifier
     */
    public void checkTypeAttributPLH(TypePLHEntity typePLH) throws AppServiceException {
        if (typePLH == null) {
            return;
        }
        if (CollectionUtils.isNotEmpty(typePLH.getFils())) {
            if (typePLH.getTypeAttributPLH() != null && typePLH.getTypeAttributPLH().equals(TypeAttributPLH.VALUE)) {
                throw new AppServiceException("Le type PLH id = " + typePLH.getId() +
                        " est une VALUE et ne peut pas avoir de fils.");
            } else {
                Set<TypePLHEntity> fils = typePLH.getFils();
                for (TypePLHEntity typePLHEntity : fils) {
                    checkTypeAttributPLH(typePLHEntity);
                }
            }
        }
    }

    /**
     * Met à jour de façon récursive toutes les valeurs qui auraient changé dans
     * l'arborescence du type PLH pour un programme
     *
     * @param typePLH         type PLH à mettre à jour
     * @param programmeEntity le programmeEntity duquel est rattaché le typePLH
     */
    public void updateValuesTypePLH(TypePLH typePLH, ProgrammeEntity programmeEntity) {
        if (typePLH == null) {
            return;
        }
        // Si c'est une catégorie
        if (typePLH.getTypeAttributPLH() == TypePLH.TypeAttributPLHEnum.CATEGORY) {
            updateValuesTypeCategoryPLH(typePLH, programmeEntity);
        }
        // sinon, c'est donc un type VALUE
        else {
            updateValuesTypeValuePLH(typePLH, programmeEntity);
        }
    }

    private void updateValuesTypeValuePLH(TypePLH typePLH, ProgrammeEntity programmeEntity) {
        if (programmeEntity == null) {
            return;
        }
        // on récupère les attributs du programme
        Set<AttributPLHEntity> attributsPLH = programmeEntity.getAttributsPLH();
        if (CollectionUtils.isNotEmpty(attributsPLH)) {
            // on fait setValue() si on trouve un attribut correspondant à notre type PLH
            for (AttributPLHEntity attributPLHEntity : attributsPLH) {
                if (attributPLHEntity != null && attributPLHEntity.getType() != null &&
                        Objects.equals(attributPLHEntity.getType().getId(), typePLH.getId())) {
                    attributPLHEntity.setValue(typePLH.getValue());
                    attributPLHDao.save(attributPLHEntity);
                    return;
                }
            }
        }
        // sinon, on la créé et on l'ajoute
        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setValue(typePLH.getValue());
        attributPLHEntity.setType(typePlhDao.findOneById(typePLH.getId()));
        attributPLHEntity = attributPLHDao.save(attributPLHEntity);
        programmeEntity.addAttributPLHProgramme(attributPLHEntity);
    }

    private void updateValuesTypeCategoryPLH(TypePLH typePLH, ProgrammeEntity programmeEntity) {
        if (CollectionUtils.isNotEmpty(typePLH.getFils())) {
            // Alors, on réutilise récursivement updateValuesTypePlh pour voir si les fils
            // sont à mettre à jour
            for (TypePLH typePLHFils : typePLH.getFils()) {
                if (typePLHFils != null) {
                    updateValuesTypePLH(typePLHFils, programmeEntity);
                }
            }
        }
    }

    /**
     * Met à jour de façon récursive toutes les valeurs qui auraient changé dans
     * l'arborescence du type PLH pour une opération
     *
     * @param typePLH         type PLH à mettre à jour
     * @param operationEntity le operationEntity duquel est rattaché le typePLH
     */
    public void updateValuesTypePLH(TypePLH typePLH, OperationEntity operationEntity) {
        if (typePLH == null) {
            return;
        }
        if (typePLH.getTypeAttributPLH() == TypePLH.TypeAttributPLHEnum.CATEGORY) {
            updateValuesTypeCategoryPLH(typePLH, operationEntity);
        } else {
            updateValuesTypeValuePLH(typePLH, operationEntity);
        }
    }

    private void updateValuesTypeValuePLH(TypePLH typePLH, OperationEntity operationEntity) {
        if (operationEntity == null) {
            return;
        }
        Set<AttributPLHEntity> attributsPLH = operationEntity.getAttributsPLH();
        Optional<AttributPLHEntity> attributExistant = CollectionUtils.isEmpty(attributsPLH) ? Optional.empty()
                : attributsPLH.stream()
                        .filter(a -> a != null && a.getType() != null
                                && Objects.equals(a.getType().getId(), typePLH.getId()))
                        .findFirst();
        if (attributExistant.isPresent()) {
            AttributPLHEntity attributPLHExistant = attributExistant.get();
            attributPLHExistant.setValue(typePLH.getValue());
            attributPLHDao.save(attributPLHExistant);
            return;
        }
        // sinon, on la créé et on l'ajoute
        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setValue(typePLH.getValue());
        attributPLHEntity.setType(typePlhDao.findOneById(typePLH.getId()));
        attributPLHEntity = attributPLHDao.save(attributPLHEntity);
        operationEntity.addAttributPLHOperation(attributPLHEntity);
    }

    private void updateValuesTypeCategoryPLH(TypePLH typePLH, OperationEntity operationEntity) {
        if (CollectionUtils.isNotEmpty(typePLH.getFils())) {
            for (TypePLH typePLHFils : typePLH.getFils()) {
                if (typePLHFils != null) {
                    updateValuesTypePLH(typePLHFils, operationEntity);
                }
            }
        }
    }
}
