package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.Vocation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.VocationEntity;

@Mapper(componentModel = "spring")
public interface VocationMapper extends AbstractMapper<VocationEntity, Vocation> {

    @Named("dtoToNewVocationEntity")
    VocationEntity dtoToEntity(Vocation vocation);
}
