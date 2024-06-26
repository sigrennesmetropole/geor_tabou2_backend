package rm.tabou2.service.mapper.tabou.document;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.mapper.AbstractMapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper extends AbstractMapper<AlfrescoDocument, DocumentMetadata> {

    @Mapping(source = "entry.id", target = "id")
    @Mapping(source = "entry.name", target = "nom")
    @Mapping(source = "entry.properties.libelleTypeDocument", target = "libelleTypeDocument")
    @Mapping(source = "entry.properties.dateDocument", target="dateDocument")
    @Mapping(source = "entry.content.mimeType", target = "typeMime")
    @Mapping(source = "entry.modifiedByUser.displayName", target = "modifUser")
    @Mapping(source = "entry.modifiedAt", target = "modifDate")
    DocumentMetadata entityToDto(AlfrescoDocument alfrescoDocument);

}
