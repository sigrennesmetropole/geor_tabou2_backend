package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.ContactTiersEntity;
import rm.tabou2.service.dto.ContactTiers;

@Mapper(componentModel = "spring")
public interface ContactTiersMapper extends AbstractMapper<ContactTiersEntity, ContactTiers> {

    @Override
    @Mapping(target = "fonctionContact", ignore = true)
    void dtoToEntity(ContactTiers dto, @MappingTarget ContactTiersEntity entity);

    @Override
    @Mapping(target = "fonctionContact", ignore = true)
    ContactTiersEntity dtoToEntity(ContactTiers dto);
}
