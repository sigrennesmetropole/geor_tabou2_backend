package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.storage.tabou.entity.TiersEntity;

@Mapper(componentModel = "spring")
public interface TiersMapper extends AbstractMapper<TiersEntity, Tiers> {
}
