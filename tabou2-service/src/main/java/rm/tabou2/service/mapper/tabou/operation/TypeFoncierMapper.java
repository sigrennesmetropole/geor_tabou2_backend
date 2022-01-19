package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeFoncier;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeFoncierEntity;

@Mapper(componentModel = "spring")
public interface TypeFoncierMapper extends AbstractMapper<TypeFoncierEntity, TypeFoncier> {
}
