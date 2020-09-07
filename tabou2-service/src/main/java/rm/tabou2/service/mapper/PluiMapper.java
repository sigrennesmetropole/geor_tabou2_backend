package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.storage.tabou.entity.administratif.PluiEntity;

@Mapper(componentModel = "spring")
public interface PluiMapper extends AbstractMapper<PluiEntity, PluiZonage> {

}
