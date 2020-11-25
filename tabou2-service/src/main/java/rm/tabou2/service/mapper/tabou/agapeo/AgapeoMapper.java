package rm.tabou2.service.mapper.tabou.agapeo;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Agapeo;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

@Mapper(componentModel = "spring")
public interface AgapeoMapper extends AbstractMapper<AgapeoEntity, Agapeo> {
}
