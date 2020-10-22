package rm.tabou2.service.tabou.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.mapper.tabou.document.TypeDocumentMapper;
import rm.tabou2.service.tabou.document.TypeDocumentService;
import rm.tabou2.storage.tabou.dao.document.TypeDocumentCustomDao;
import rm.tabou2.storage.tabou.dao.document.TypeDocumentDao;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TypeDocumentServiceImpl implements TypeDocumentService {

    @Autowired
    private TypeDocumentDao typeDocumentDao;

    @Autowired
    private TypeDocumentCustomDao typeDocumentCustomDao;

    @Autowired
    private TypeDocumentMapper typeDocumentMapper;

    @Autowired
    private AuthentificationHelper authentificationHelper;

    @Override
    public TypeDocument createTypeDocument(TypeDocument typeDocument) throws AppServiceException {

        TypeDocumentEntity typeDocumentEntity = typeDocumentMapper.dtoToEntity(typeDocument);

        //Vérification des champs obligatoires
        if (typeDocumentEntity.getLibelle().isEmpty()) {
            throw new AppServiceException("Le champ libelle est manquant");
        }

        //Historisation
        typeDocumentEntity.setCreateDate(new Date());
        typeDocumentEntity.setCreateUser(authentificationHelper.getConnectedUsername());

        try {
            typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter un type document ", e);
        }

        return typeDocumentMapper.entityToDto(typeDocumentEntity);

    }

    @Override
    public TypeDocument updateTypeDocument(TypeDocument typeDocument) throws AppServiceException {

        TypeDocumentEntity typeDocumentEntity;

        Optional<TypeDocumentEntity> typeDocumentEntityOpt = typeDocumentDao.findById(typeDocument.getId());

        if (typeDocumentEntityOpt.isEmpty()) {
            throw new NoSuchElementException("Le type de document id = " + typeDocument.getId() + " n'existe pas");
        } else {
            typeDocumentEntity = typeDocumentEntityOpt.get();
        }

        typeDocumentEntity.setDateInactif(typeDocument.getDateInactif());

        typeDocumentEntity.setLibelle(typeDocument.getLibelle());

        // Enregistrement en BDD
        try {
            typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'éditer le type de document " + typeDocument.getId(), e);
        }

        return typeDocumentMapper.entityToDto(typeDocumentEntity);
    }

    @Override
    public TypeDocument inactivateTypeDocument(Long typeDocumentId) throws AppServiceException {

        TypeDocumentEntity typeDocument;

        Optional<TypeDocumentEntity> typeDocumentOpt = typeDocumentDao.findById(typeDocumentId);
        if (typeDocumentOpt.isEmpty()) {
            throw new NoSuchElementException("Le type de document id=" + typeDocumentId + " n'existe pas");
        } else {
            typeDocument = typeDocumentOpt.get();
        }

        typeDocument.setDateInactif(new Date());

        // Enregistrement en BDD
        try {
            typeDocument = typeDocumentDao.save(typeDocument);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactive le type document " + typeDocument.getId(), e);
        }

        return typeDocumentMapper.entityToDto(typeDocument);
    }

    @Override
    public Page<TypeDocument> searchTypeDocument(TypeDocumentCriteria typeDocumentCriteria, Pageable pageable) {

        Page<TypeDocumentEntity> typesDocuments = typeDocumentCustomDao.searchTypeDocument(typeDocumentCriteria, pageable);

        return typeDocumentMapper.entitiesToDto(typesDocuments, pageable);

    }

}
