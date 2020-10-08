package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;

@Mapper(componentModel = "spring")
public interface EtapeOperationMapper extends AbstractMapper<EtapeOperationEntity, Etape> {

}
