package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.Decision;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.DecisionEntity;

@Mapper(componentModel = "spring")
public interface DecisionMapper extends AbstractMapper<DecisionEntity, Decision> {

    @Named("dtoToNewDecisionEntity")
    DecisionEntity dtoToEntity(Decision decision);
}
