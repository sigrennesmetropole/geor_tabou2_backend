package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.PlhEntity;
import rm.tabou2.service.dto.Plh;

@Mapper(componentModel = "spring")
public interface PlhMapper extends AbstractMapper<PlhEntity, Plh> {
}
