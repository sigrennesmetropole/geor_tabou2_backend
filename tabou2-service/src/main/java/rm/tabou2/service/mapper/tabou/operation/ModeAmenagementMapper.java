package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.ModeAmenagement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.ModeAmenagementEntity;

@Mapper(componentModel = "spring")
public interface ModeAmenagementMapper extends AbstractMapper<ModeAmenagementEntity, ModeAmenagement> {

    @Named("dtoToNewModeAmenagementEntity")
    ModeAmenagementEntity dtoToEntity(ModeAmenagement modeAmenagement);
}
