package rm.tabou2.service.tabou.document.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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

@Service
@Validated
@Transactional(readOnly = true)
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
    @Transactional
    public TypeDocument createTypeDocument(TypeDocument typeDocument) throws AppServiceException {

        if (!authentificationHelper.hasEditAccess()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de création " +
                    "du type de document " + typeDocument.getLibelle());
        }

        TypeDocumentEntity typeDocumentEntity = typeDocumentMapper.dtoToEntity(typeDocument);

        try {
            typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'ajouter un type document ", e);
        }

        return typeDocumentMapper.entityToDto(typeDocumentEntity);

    }

    @Override
    @Transactional
    public TypeDocument updateTypeDocument(TypeDocument typeDocument) throws AppServiceException {

        if (!authentificationHelper.hasEditAccess()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification " +
                    "du type de document " + typeDocument.getLibelle());
        }

        TypeDocumentEntity typeDocumentEntity = typeDocumentDao.findOneById(typeDocument.getId());

        typeDocumentMapper.dtoToEntity(typeDocument, typeDocumentEntity);

        // Enregistrement en BDD
        try {
            typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible d'éditer le type de document " + typeDocument.getId(), e);
        }

        return typeDocumentMapper.entityToDto(typeDocumentEntity);
    }

    @Override
    @Transactional
    public TypeDocument inactivateTypeDocument(Long typeDocumentId) throws AppServiceException {

        if (!authentificationHelper.hasEditAccess()) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de modification " +
                    "du type de document id = " + typeDocumentId);
        }

        TypeDocumentEntity typeDocumentEntity = typeDocumentDao.findOneById(typeDocumentId);

        typeDocumentEntity.setDateInactif(new Date());

        // Enregistrement en BDD
        try {
            typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);
        } catch (DataAccessException e) {
            throw new AppServiceException("Impossible de rendre inactive le type document " + typeDocumentEntity.getId(), e);
        }

        return typeDocumentMapper.entityToDto(typeDocumentEntity);
    }

    @Override
    public Page<TypeDocument> searchTypeDocument(TypeDocumentCriteria typeDocumentCriteria, Pageable pageable) {

        Page<TypeDocumentEntity> typesDocuments = typeDocumentCustomDao.searchTypeDocument(typeDocumentCriteria, pageable);

        return typeDocumentMapper.entitiesToDto(typesDocuments, pageable);

    }

    @Override
    public TypeDocument getTypeDocumentById(Long typeDocumentId) {

        TypeDocumentEntity typeDocumentEntity = typeDocumentDao.findOneById(typeDocumentId);

        return typeDocumentMapper.entityToDto(typeDocumentEntity);
    }

}
