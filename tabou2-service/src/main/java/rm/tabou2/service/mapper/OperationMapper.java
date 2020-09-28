package rm.tabou2.service.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.storage.tabou.entity.OperationEntity;

@Mapper(componentModel = "spring")
public interface OperationMapper extends AbstractMapper<OperationEntity, Operation> {

    @Mappings({ @Mapping(source = "etapeOperation", target = "etape")})
    @Override
    Operation entityToDto(OperationEntity operationEntity);
}
