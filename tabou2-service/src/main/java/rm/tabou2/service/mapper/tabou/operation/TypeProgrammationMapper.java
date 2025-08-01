package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;

import rm.tabou2.service.dto.TypeProgrammation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeProgrammationEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface TypeProgrammationMapper extends AbstractMapper<TypeProgrammationEntity, TypeProgrammation> {
}
