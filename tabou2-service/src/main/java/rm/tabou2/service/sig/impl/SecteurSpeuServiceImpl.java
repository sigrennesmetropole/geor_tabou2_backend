package rm.tabou2.service.sig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.helper.operation.SecteurFicheHelper;
import rm.tabou2.service.mapper.sig.SecteurSpeuMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.service.sig.SecteurSpeuService;
import rm.tabou2.service.st.generator.DocumentGenerator;
import rm.tabou2.service.st.generator.model.DocumentContent;
import rm.tabou2.service.st.generator.model.GenerationModel;
import rm.tabou2.storage.sig.dao.SecteurSpeuCustomDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
@Validated
@Transactional(readOnly = true)
public class SecteurSpeuServiceImpl implements SecteurSpeuService {

    @Autowired
    private SecteurSpeuCustomDao secteurSpeuCustomDao;

    @Autowired
    private SecteurSpeuMapper secteurSpeuMapper;

    @Autowired
    private OperationRightsHelper operationRightsHelper;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private DocumentGenerator documentGenerator;

    @Autowired
    private SecteurFicheHelper secteurFicheHelper;

    @Override
    public Page<SecteurSpeu> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable) {

        return secteurSpeuMapper.entitiesToDto(secteurSpeuCustomDao.searchSecteursSpeu(numSecteur, nomSecteur, pageable), pageable);

    }

    @Override
    public DocumentContent generateFicheSuivi(Long operationId) throws AppServiceException {
        OperationEntity operationEntity = getOperationEntityById(operationId);

    if (Boolean.FALSE.equals(operationEntity.getSecteur())) {
            throw new AppServiceException("Erreur de génération de la fiche de suivi Secteur : l'opération n'est pas un secteur.");
        }
        GenerationModel generationModel = secteurFicheHelper.buildGenerationModel(getOperationEntityById(operationId));


        DocumentContent documentContent = documentGenerator.generateDocument(generationModel);
        documentContent.setFileName(buildRapportFileName(operationEntity));
        return documentContent;
    }

    private OperationEntity getOperationEntityById(long operationId) {

        OperationEntity operationEntity = operationDao.findOneById(operationId);

        if (!operationRightsHelper.checkCanGetOperation(operationMapper.entityToDto(operationEntity))) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer l'opération id = " + operationId);
        }

        return operationEntity;
    }

    private String buildRapportFileName(OperationEntity operationEntity) {
        StringBuilder fileName = new StringBuilder("FicheSuivi_");
        fileName.append(operationEntity.getId());
        fileName.append("_");
        fileName.append(operationEntity.getCode());
        fileName.append("_");
        fileName.append(operationEntity.getNom());
        fileName.append("_");
        fileName.append(System.nanoTime());

        return fileName.toString();
    }

}
