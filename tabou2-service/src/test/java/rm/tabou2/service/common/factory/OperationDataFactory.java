package rm.tabou2.service.common.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.mapper.tabou.operation.EtapeOperationMapper;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.service.mapper.tabou.operation.VocationMapper;
import rm.tabou2.service.constant.NatureLibelle;
import rm.tabou2.service.constant.VocationCode;
import rm.tabou2.storage.sig.dao.SecteurDao;
import rm.tabou2.storage.sig.entity.SecteurEntity;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.NatureDao;
import rm.tabou2.storage.tabou.dao.operation.VocationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;

/**
 * Factory de création d'objets de test liés aux opérations.
 * Mutualisée entre les différents tests.
 */
@Component
@RequiredArgsConstructor
public class OperationDataFactory {

    private final SecteurDao secteurDao;
    private final NatureDao natureDao;
    private final VocationDao vocationDao;
    private final EtapeOperationDao etapeOperationDao;
    private final EtapeOperationMapper etapeMapper;
    private final NatureMapper natureMapper;
    private final VocationMapper vocationMapper;

    /**
     * Crée ou récupère un secteur de test en base.
     */
    public SecteurEntity getOrCreateSecteur(int id) {
        return secteurDao.findById(id)
                .orElseGet(() -> {
                    SecteurEntity secteur = new SecteurEntity();
                    secteur.setId(id);
                    return secteurDao.save(secteur);
                });
    }

    /**
     * Construit une opération intermédiaire de base avec secteur, nature, étape et vocation pré-configurés.
     * Le secteur est créé en base automatiquement.
     */
    public OperationIntermediaire createBaseOperation() {
        return createBaseOperation("operationTestLS", "codeTestLS");
    }

    /**
     * Construit une opération intermédiaire de base avec des paramètres personnalisés.
     */
    public OperationIntermediaire createBaseOperation(String nom, String code) {
        SecteurEntity secteurEntity = getOrCreateSecteur(1);

        NatureEntity natureEntity = natureDao.findByLibelle(NatureLibelle.ZAC);
        EtapeOperationEntity etape = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");
        VocationEntity vocationEntity = vocationDao.findByCode(VocationCode.ESPACE_VERT);

        OperationIntermediaire operation = new OperationIntermediaire();
        operation.setNom(nom);
        operation.setCode(code);
        operation.setDiffusionRestreinte(false);
        operation.setSecteur(true);
        operation.setEtape(etapeMapper.entityToDto(etape));
        operation.setNature(natureMapper.entityToDto(natureEntity));
        operation.setVocation(vocationMapper.entityToDto(vocationEntity));
        operation.setIdEmprise(secteurEntity.getId().longValue());
        return operation;
    }
}

