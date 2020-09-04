package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.storage.tabou.entity.TypeDocumentEntity;

@Mapper(componentModel = "spring")
public interface TypeDocumentMapper extends AbstractMapper<TypeDocumentEntity, TypeDocument> {

}
