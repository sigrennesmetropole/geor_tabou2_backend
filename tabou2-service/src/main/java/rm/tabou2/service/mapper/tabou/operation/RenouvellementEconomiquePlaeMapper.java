package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;

import rm.tabou2.service.dto.RenouvellementEconomiquePlae;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.RenouvellementEconomiquePlaeEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface RenouvellementEconomiquePlaeMapper
        extends AbstractMapper<RenouvellementEconomiquePlaeEntity, RenouvellementEconomiquePlae> {
}
