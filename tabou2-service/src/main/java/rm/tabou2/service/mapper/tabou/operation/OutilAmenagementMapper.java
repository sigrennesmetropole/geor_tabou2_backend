package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.OutilAmenagement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OutilAmenagementEntity;

@Mapper(componentModel = "spring")
public interface OutilAmenagementMapper extends AbstractMapper<OutilAmenagementEntity, OutilAmenagement> {

    @Named("dtoToNewOutilAmenagementEntity")
    OutilAmenagementEntity dtoToEntity(OutilAmenagement outilAmenagement);
}
