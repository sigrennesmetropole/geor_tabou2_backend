package rm.tabou2.service.mapper.tabou.plh;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

@Mapper(componentModel = "spring")
public interface TypePLHMapper extends AbstractMapper<TypePLHEntity, TypePLH> {

    TypePLHEntity dtoToEntity(TypePLH dto);

    TypePLH entityToDto(TypePLHEntity entity);
}
