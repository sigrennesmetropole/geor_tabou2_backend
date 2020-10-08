package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

@Mapper(componentModel = "spring")
public interface TiersMapper extends AbstractMapper<TiersEntity, Tiers> {
}
