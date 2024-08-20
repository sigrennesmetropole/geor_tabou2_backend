package rm.tabou2.service.helper.operation;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rm.tabou2.service.constant.NatureLibelle;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.storage.sig.dao.EnDiffusDao;
import rm.tabou2.storage.sig.dao.SecteurDao;
import rm.tabou2.storage.sig.dao.ZaDao;
import rm.tabou2.storage.sig.dao.ZacDao;
import rm.tabou2.storage.sig.entity.EnDiffusEntity;
import rm.tabou2.storage.sig.entity.SecteurEntity;
import rm.tabou2.storage.sig.entity.ZaEntity;
import rm.tabou2.storage.sig.entity.ZacEntity;
import rm.tabou2.storage.tabou.dao.operation.NatureDao;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class OperationEmpriseHelper {

    @Autowired
    private ZacDao zacDao;

    @Autowired
    private ZaDao zaDao;

    @Autowired
    private SecteurDao secteurDao;

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private EnDiffusDao enDiffusDao;

    public void saveEmprise(OperationIntermediaire operationToSave, Long idEmprise) {

        NatureEntity nature = natureDao.findById(operationToSave.getNature().getId()).orElseThrow(() -> new EntityNotFoundException("Le nature id={0} n'existe pas"));
        if (BooleanUtils.isTrue(operationToSave.getSecteur())) {
            SecteurEntity secteurEntity = secteurDao.findOneById(idEmprise.intValue());
            secteurEntity.setIdTabou(operationToSave.getId().intValue());
            secteurDao.save(secteurEntity);
        } else if (NatureLibelle.ZAC.equalsIgnoreCase(nature.getLibelle())) {
            ZacEntity zacEntity = zacDao.findOneById(idEmprise.intValue());
            zacEntity.setIdTabou(operationToSave.getId().intValue());
            zacDao.save(zacEntity);
        } else if (NatureLibelle.ZA.equalsIgnoreCase(nature.getLibelle())) {
            ZaEntity zaEntity = zaDao.findOneById(idEmprise.intValue());
            zaEntity.setIdTabou(operationToSave.getId().intValue());
            zaDao.save(zaEntity);
        } else if (NatureLibelle.EN_DIFFUS.equalsIgnoreCase(nature.getLibelle())) {
            EnDiffusEntity enDiffusEntity = enDiffusDao.findOneById(idEmprise.intValue());
            enDiffusEntity.setIdTabou(operationToSave.getId().intValue());
            enDiffusDao.save(enDiffusEntity);
        }
    }

    @Named("getIdEmpriseOperation")
    public Long getIdEmprise(OperationEntity operationEntity) {
        if(operationEntity.getNature() == null){
            return null;
        }
        NatureEntity nature = natureDao.findById(operationEntity.getNature().getId()).orElseThrow(() -> new EntityNotFoundException("Le nature id={0} n'existe pas"));
        if (BooleanUtils.isTrue(operationEntity.getSecteur())) {
            SecteurEntity secteurEntity = secteurDao.getByIdTabou(Math.toIntExact(operationEntity.getId()));
            return secteurEntity != null ? secteurEntity.getId().longValue() : null;
        } else if (NatureLibelle.ZAC.equalsIgnoreCase(nature.getLibelle())) {
            ZacEntity zacEntity = zacDao.getByIdTabou(Math.toIntExact(operationEntity.getId()));
            return zacEntity != null ? zacEntity.getId().longValue() : null;
        } else if (NatureLibelle.ZA.equalsIgnoreCase(nature.getLibelle())) {
            ZaEntity zaEntity = zaDao.getByIdTabou(Math.toIntExact(operationEntity.getId()));
            return zaEntity != null ? zaEntity.getId().longValue() : null;
        } else if (NatureLibelle.EN_DIFFUS.equalsIgnoreCase(nature.getLibelle())) {
            EnDiffusEntity enDiffusEntity = enDiffusDao.getByIdTabou(Math.toIntExact(operationEntity.getId()));
            return enDiffusEntity != null ? enDiffusEntity.getId().longValue() : null;
        }
        return null;
    }

    public Page<Emprise> getAvailableEmprises(Long natureId, Boolean estSecteur, Pageable pageable, String nom) {

        List<Emprise> result = new ArrayList<>();
        int totalResultsNumber = 0;

        Optional<NatureEntity> natureEntityOpt = natureDao.findById(natureId);

        if (natureEntityOpt.isEmpty()) {
            throw new NoSuchElementException("La nature id=" + natureId + " n'existe pas");
        }

        if (StringUtils.isEmpty(nom)) {
            nom = "%";
        } else if (StringUtils.endsWith(nom, "*")) {
            nom = nom.replaceAll("\\*", "%");
        }

        String libelleNature = natureEntityOpt.get().getLibelle();

        if (BooleanUtils.isTrue(estSecteur)) {
            List<SecteurEntity> secteurEntities = secteurDao.findAllByIdTabouIsNullAndSecteurIsLikeIgnoreCase(nom, pageable);
            totalResultsNumber = secteurDao.countAllByIdTabouIsNullAndSecteurIsLikeIgnoreCase(nom);
            result = secteurEntities.stream().map(secteurEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(secteurEntity.getId().longValue());
                emprise.setNom(secteurEntity.getSecteur());
                return emprise;
            }).toList();
        } else if (NatureLibelle.ZAC.equalsIgnoreCase(libelleNature)) {
            List<ZacEntity> zacEntities = zacDao.findAllByIdTabouIsNullAndNomZacIsLikeIgnoreCase(nom, pageable);
            totalResultsNumber = zacDao.countAllByIdTabouIsNullAndNomZacIsLikeIgnoreCase(nom);
            result = zacEntities.stream().map(zacEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zacEntity.getId().longValue());
                emprise.setNom(zacEntity.getNomZac());
                return emprise;
            }).toList();
        } else if (NatureLibelle.ZA.equalsIgnoreCase(libelleNature)) {
            List<ZaEntity> zaEntities = zaDao.findAllByIdTabouIsNullAndNomZaIsLikeIgnoreCase(nom, pageable);
            totalResultsNumber = zaDao.countAllByIdTabouIsNullAndNomZaIsLikeIgnoreCase(nom);
            result = zaEntities.stream().map(zaEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zaEntity.getId().longValue());
                emprise.setNom(zaEntity.getNomZa());
                return emprise;
            }).toList();
        } else if (NatureLibelle.EN_DIFFUS.equalsIgnoreCase(libelleNature)) {
            List<EnDiffusEntity> enDiffusEntities = enDiffusDao.findAllByName(nom, pageable);
            totalResultsNumber = enDiffusDao.countAllByIdTabouIsNullAndNomIsLikeIgnoreCase(nom);
            result = enDiffusEntities.stream().map(zaEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zaEntity.getId().longValue());
                emprise.setNom(zaEntity.getNom());
                return emprise;
            }).toList();
        }

        return new PageImpl<>(result, pageable, totalResultsNumber);
    }
}
