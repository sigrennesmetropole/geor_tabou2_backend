package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.storage.tabou.entity.administratif.QuartierEntity;

@Mapper(componentModel = "spring")
public interface QuartierMapper extends AbstractMapper<QuartierEntity, Quartier> {

}
