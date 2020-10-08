package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Mapper(componentModel = "spring")
public interface OperationMapper extends AbstractMapper<OperationEntity, Operation> {

    @Mapping(source = "etapeOperation", target = "etape")
    @Override
    Operation entityToDto(OperationEntity operationEntity);
}
