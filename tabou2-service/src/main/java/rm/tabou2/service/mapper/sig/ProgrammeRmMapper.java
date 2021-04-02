package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Emprise;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;

@Mapper(componentModel = "spring")
public interface ProgrammeRmMapper extends AbstractMapper<ProgrammeRmEntity, Emprise> {

    @Mapping(source = "programme", target = "nom")
    Emprise entityToDto(ProgrammeRmEntity programmeRmEntity);


}
