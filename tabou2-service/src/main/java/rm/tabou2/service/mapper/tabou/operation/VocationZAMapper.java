package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.VocationZA;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.VocationZAEntity;

@Mapper(componentModel = "spring")
public interface VocationZAMapper extends AbstractMapper<VocationZAEntity, VocationZA> {
}
