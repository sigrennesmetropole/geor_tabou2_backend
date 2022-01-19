package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.FonctionContactsEntity;
import rm.tabou2.service.dto.FonctionContacts;

@Mapper(componentModel = "spring")
public interface FonctionContactsMapper extends AbstractMapper<FonctionContactsEntity, FonctionContacts> {
}
