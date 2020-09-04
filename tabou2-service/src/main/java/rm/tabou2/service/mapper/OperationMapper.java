package rm.tabou2.service.mapper;


import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.tabou.entity.OperationEntity;

@Mapper(componentModel = "spring")
public interface OperationMapper extends AbstractMapper<OperationEntity, Operation> {
}
