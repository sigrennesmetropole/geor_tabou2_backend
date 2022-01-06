package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeActionOperation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeActionOperationEntity;

@Mapper(componentModel = "spring")
public interface TypeActionOperationMapper extends AbstractMapper<TypeActionOperationEntity, TypeActionOperation> {
}
