package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.DescriptionConcertationEntity;
import rm.tabou2.service.dto.DescriptionConcertation;

@Mapper(componentModel = "spring")
public interface DescriptionConcertationMapper extends AbstractMapper<DescriptionConcertationEntity, DescriptionConcertation> {
}
