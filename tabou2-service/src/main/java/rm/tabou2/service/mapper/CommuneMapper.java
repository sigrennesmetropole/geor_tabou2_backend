package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Commune;
import rm.tabou2.storage.tabou.entity.administratif.CommuneEntity;

@Mapper(componentModel = "spring")
public interface CommuneMapper extends AbstractMapper<CommuneEntity, Commune> {


}
