package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeTiers;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

@Mapper(componentModel = "spring")
public interface TypeTiersMapper extends AbstractMapper<TypeTiersEntity, TypeTiers> {

}
