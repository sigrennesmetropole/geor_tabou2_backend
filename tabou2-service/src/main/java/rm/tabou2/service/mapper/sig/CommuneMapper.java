package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.CommuneEntity;

@Mapper(componentModel = "spring")
public interface CommuneMapper extends AbstractMapper<CommuneEntity, Commune> {


}
