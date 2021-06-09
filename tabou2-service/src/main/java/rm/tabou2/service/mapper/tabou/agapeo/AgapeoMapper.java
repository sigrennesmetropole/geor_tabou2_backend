package rm.tabou2.service.mapper.tabou.agapeo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

@Mapper(componentModel = "spring")
public interface AgapeoMapper extends AbstractMapper<AgapeoEntity, Agapeo> {

    @Mapping(source = "logementsLocatifAide", target = "logementsLocatAide")
    @Mapping(source = "logementsAccessAide", target = "logementsAccessAide")
    @Mapping(source = "logementsLocatifRegulePrive", target = "logementsLocatRegulPrive")
    @Mapping(source = "logementsLocatifReguleHlm", target = "logementsLocatRegulHlm")
    @Mapping(source = "logementsAccessMaitrise", target = "logementsAccessMaitrise")
    Agapeo entityToDto(AgapeoEntity agapeoEntity);

}
