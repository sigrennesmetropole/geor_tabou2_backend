package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeActeur;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeActeurEntity;

@Mapper(componentModel = "spring")
public interface TypeActeurMapper extends AbstractMapper<TypeActeurEntity, TypeActeur> {
}
