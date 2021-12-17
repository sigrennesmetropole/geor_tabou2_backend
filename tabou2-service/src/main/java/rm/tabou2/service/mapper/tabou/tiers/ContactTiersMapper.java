package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.service.dto.ContactTiers;

@Mapper(componentModel = "spring")
public interface ContactTiersMapper extends AbstractMapper<ContactTiersEntity, ContactTiers> {
}
