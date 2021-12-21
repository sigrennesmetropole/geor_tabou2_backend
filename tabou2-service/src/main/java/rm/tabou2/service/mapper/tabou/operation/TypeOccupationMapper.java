package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.dto.TypeOccupation;
import rm.tabou2.storage.tabou.entity.operation.TypeOccupationEntity;

@Mapper(componentModel = "spring")
public interface TypeOccupationMapper extends AbstractMapper<TypeOccupationEntity, TypeOccupation> {
}
