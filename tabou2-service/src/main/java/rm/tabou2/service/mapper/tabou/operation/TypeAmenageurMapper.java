package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeAmenageurEntity;
import rm.tabou2.service.dto.TypeAmenageur;

@Mapper(componentModel = "spring")
public interface TypeAmenageurMapper extends AbstractMapper<TypeAmenageurEntity, TypeAmenageur> {
}
