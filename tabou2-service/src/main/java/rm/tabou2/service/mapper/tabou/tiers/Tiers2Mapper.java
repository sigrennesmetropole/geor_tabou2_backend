package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

@Mapper(componentModel = "spring")
public interface Tiers2Mapper extends AbstractMapper<TiersEntity, Tiers> {

    @Override
    @Mapping(target = "adresseNum", ignore = true)
    @Mapping(target = "adresseRue", ignore = true)
    @Mapping(target = "contact", ignore = true)
    TiersEntity dtoToEntity(Tiers dto);

    @Override
    @Mapping(target = "adresseNum", ignore = true)
    @Mapping(target = "adresseRue", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Tiers entityToDto(TiersEntity entity);
}
