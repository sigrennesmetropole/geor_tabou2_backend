package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.PluiZonage;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.PluiEntity;

@Mapper(componentModel = "spring")
public interface PluiMapper extends AbstractMapper<PluiEntity, PluiZonage> {

}
