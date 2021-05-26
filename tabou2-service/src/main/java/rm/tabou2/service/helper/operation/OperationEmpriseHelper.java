package rm.tabou2.service.helper.operation;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rm.tabou2.service.constant.NatureLibelle;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.dto.Operation;
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

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void saveEmprise(Operation operationToSave, Long idEmprise) {

        Nature nature = operationToSave.getNature();
        if (BooleanUtils.isTrue(operationToSave.isSecteur())) {
            SecteurEntity secteurEntity = secteurDao.findOneById(idEmprise.intValue());
            secteurEntity.setIdTabou(operationToSave.getId().intValue());
            secteurDao.save(secteurEntity);
        } else if (NatureLibelle.ZAC.equals(nature.getLibelle())) {
            ZacEntity zacEntity = zacDao.findOneById(idEmprise.intValue());
            zacEntity.setIdTabou(operationToSave.getId().intValue());
            zacDao.save(zacEntity);
        } else if (NatureLibelle.ZA.equals(nature.getLibelle())) {
            ZaEntity zaEntity = zaDao.findOneById(idEmprise.intValue());
            zaEntity.setIdTabou(operationToSave.getId().intValue());
            zaDao.save(zaEntity);
        } else if (NatureLibelle.EN_DIFFUS.equals(nature.getLibelle())) {
            EnDiffusEntity enDiffusEntity = enDiffusDao.findOneById(idEmprise.intValue());
            enDiffusEntity.setIdTabou(operationToSave.getId().intValue());
            enDiffusDao.save(enDiffusEntity);
        }
    }

    public List<Emprise> getAvailableEmprises(Long natureId, Boolean estSecteur) {


        Optional<NatureEntity> natureEntityOpt = natureDao.findById(natureId);

        if (natureEntityOpt.isEmpty()) {
            throw new NoSuchElementException("La nature id=" + natureId + " n'existe pas");
        }

        String libelleNature = natureEntityOpt.get().getLibelle();

        if (BooleanUtils.isTrue(estSecteur)) {
            List<SecteurEntity> secteurEntities = secteurDao.findAllByIdTabouIsNull();
            return secteurEntities.stream().map(secteurEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(secteurEntity.getId().longValue());
                emprise.setNom(secteurEntity.getSecteur());
                return emprise;
            }).collect(Collectors.toList());
        } else if (NatureLibelle.ZAC.equalsIgnoreCase(libelleNature)) {
            List<ZacEntity> zacEntities = zacDao.findAllByIdTabouIsNull();
            return zacEntities.stream().map(zacEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zacEntity.getId().longValue());
                emprise.setNom(zacEntity.getNomZac());
                return emprise;
            }).collect(Collectors.toList());
        } else if (NatureLibelle.ZA.equalsIgnoreCase(libelleNature)) {
            List<ZaEntity> zaEntities = zaDao.findAllByIdTabouIsNull();
            return zaEntities.stream().map(zaEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zaEntity.getId().longValue());
                emprise.setNom(zaEntity.getNomZa());
                return emprise;
            }).collect(Collectors.toList());
        } else if (NatureLibelle.EN_DIFFUS.equalsIgnoreCase(libelleNature)) {
            List<EnDiffusEntity> enDiffusEntities = enDiffusDao.findAllByIdTabouIsNull();
            return enDiffusEntities.stream().map(zaEntity -> {
                Emprise emprise = new Emprise();
                emprise.setId(zaEntity.getId().longValue());
                emprise.setNom(zaEntity.getNom());
                return emprise;
            }).collect(Collectors.toList());

        }
        return Collections.emptyList();
    }
}
