package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EntiteRMEntity;
import rm.tabou2.service.dto.EntiteRM;

@Mapper(componentModel = "spring")
public interface EntiteRMMapper extends AbstractMapper<EntiteRMEntity, EntiteRM> {
}
