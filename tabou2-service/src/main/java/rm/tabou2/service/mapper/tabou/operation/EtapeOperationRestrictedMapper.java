package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.dto.EtapeRestricted;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;

@Mapper(componentModel = "spring")
public interface EtapeOperationRestrictedMapper extends AbstractMapper<EtapeOperationEntity, EtapeRestricted> {

    @Named("dtoToNewEtapeOperationEntity")
    EtapeOperationEntity dtoToEntity(Etape etape);
}
