package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Quartier;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.QuartierEntity;

@Mapper(componentModel = "spring")
public interface QuartierMapper extends AbstractMapper<QuartierEntity, Quartier> {

}
