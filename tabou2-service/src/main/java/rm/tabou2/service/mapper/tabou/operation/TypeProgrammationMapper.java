package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeProgrammation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeProgrammationEntity;

@Mapper(componentModel = "spring")
public interface TypeProgrammationMapper extends AbstractMapper<TypeProgrammationEntity, TypeProgrammation> {
}
