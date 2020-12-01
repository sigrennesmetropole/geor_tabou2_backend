package rm.tabou2.service.mapper.tabou.document;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;

@Mapper(componentModel = "spring")
public interface TypeDocumentMapper extends AbstractMapper<TypeDocumentEntity, TypeDocument> {

}
