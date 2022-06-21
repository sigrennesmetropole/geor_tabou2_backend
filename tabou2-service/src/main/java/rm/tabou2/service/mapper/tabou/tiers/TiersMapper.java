package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.tiers.TiersEntity;

@Mapper(componentModel = "spring")
public interface TiersMapper extends AbstractMapper<TiersEntity, Tiers> {

    @Override
    @Mapping(target="contacts", ignore = true)
    TiersEntity dtoToEntity(Tiers dto);

    @Override
    @Mapping(target="contacts", ignore = true)
    void dtoToEntity(Tiers dto, @MappingTarget TiersEntity entity);

    @Override
    @Mapping(target="adresseRue", source="adresse")
    Tiers entityToDto(TiersEntity entity);
}
