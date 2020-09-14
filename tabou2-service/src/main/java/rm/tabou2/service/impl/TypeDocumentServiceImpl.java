package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.TypeDocumentService;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.mapper.TypeDocumentMapper;
import rm.tabou2.service.util.Utils;
import rm.tabou2.storage.tabou.dao.TypeDocumentDao;
import rm.tabou2.storage.tabou.entity.TypeDocumentEntity;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TypeDocumentServiceImpl implements TypeDocumentService {

    @Autowired
    private TypeDocumentDao typeDocumentDao;

    @Autowired
    private TypeDocumentMapper typeDocumentMapper;

    @Override
    public TypeDocument addTypeDocument(TypeDocument typeDocument) {

        TypeDocumentEntity typeDocumentEntity = typeDocumentMapper.dtoToEntity(typeDocument);

        typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);

        return typeDocumentMapper.entityToDto(typeDocumentEntity);

    }

    @Override
    public void inactivateTypeDocument(Long typeDocumentId) throws NoSuchElementException {

        Optional<TypeDocumentEntity> typeDocumentOpt = typeDocumentDao.findById(typeDocumentId);
        if (null == typeDocumentOpt || typeDocumentOpt.isEmpty()) {
            throw new NoSuchElementException("Le type de document id=" + typeDocumentId + " n'existe pas");
        }

        TypeDocumentEntity typeDocument = typeDocumentOpt.get();
        typeDocument.setDateInactif(new Date());

        typeDocumentDao.save(typeDocument);

    }

    @Override
    public List<TypeDocument> searchTypeDocument(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception {

        List<TypeDocumentEntity> typesDocuments = null;

        if (onlyActive) {
            typesDocuments = typeDocumentDao.findOnlyActiveByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));
        } else {
            typesDocuments = typeDocumentDao.findByKeyword(keyword, Utils.buildPageable(start, resultsNumber, orderBy, asc));
        }

        return typeDocumentMapper.entitiesToDto(typesDocuments);

    }

}
