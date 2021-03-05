package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EtapeProgrammeRestrictedMapper extends AbstractMapper<EtapeProgrammeEntity, EtapeRestricted> {

    @Named("dtoToNewEntity")
    EtapeOperationEntity dtoToEntity(Etape etape);
}
