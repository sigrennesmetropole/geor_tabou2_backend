package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.storage.tabou.entity.habitat.AgapeoEntity;

@Mapper(componentModel = "spring")
public interface AgapeoMapper extends AbstractMapper<AgapeoEntity, Agapeo> {

    @Mapping(source = "logementsLocatifAide", target = "logementsLocatAide")
    @Mapping(source = "logementsLocatifRegulePrive", target = "logementsLocatRegulPrive")
    @Mapping(source = "logementsLocatifReguleHlm", target = "logementsLocatRegulHlm")
    AgapeoEntity dtoToEntity(Agapeo dto);

    @Mapping(source = "logementsLocatAide", target = "logementsLocatifAide")
    @Mapping(source = "logementsLocatRegulPrive", target = "logementsLocatifRegulePrive")
    @Mapping(source = "logementsLocatRegulHlm", target = "logementsLocatifReguleHlm")
    Agapeo entityToDto(AgapeoEntity entity);
}
