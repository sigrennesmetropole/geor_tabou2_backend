package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeTiers;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.TypeTiersEntity;

@Mapper(componentModel = "spring")
public interface TypeTiersMapper extends AbstractMapper<TypeTiersEntity, TypeTiers> {
}
