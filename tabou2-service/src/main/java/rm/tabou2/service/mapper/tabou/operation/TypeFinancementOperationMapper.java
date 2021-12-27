package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeFinancementOperationEntity;
import rm.tabou2.service.dto.TypeFinancementOperation;

@Mapper(componentModel = "spring")
public interface TypeFinancementOperationMapper extends AbstractMapper<TypeFinancementOperationEntity, TypeFinancementOperation> {
}
