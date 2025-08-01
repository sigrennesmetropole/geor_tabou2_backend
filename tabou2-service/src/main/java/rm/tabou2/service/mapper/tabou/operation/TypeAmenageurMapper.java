package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;

import rm.tabou2.service.dto.TypeAmenageur;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeAmenageurEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface TypeAmenageurMapper extends AbstractMapper<TypeAmenageurEntity, TypeAmenageur> {
}
