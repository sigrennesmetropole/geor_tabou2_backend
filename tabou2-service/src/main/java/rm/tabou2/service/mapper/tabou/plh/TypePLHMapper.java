package rm.tabou2.service.mapper.tabou.plh;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

import java.util.Comparator;

@Mapper(componentModel = "spring")
public interface TypePLHMapper extends AbstractMapper<TypePLHEntity, TypePLH> {

    TypePLHEntity dtoToEntity(TypePLH dto);

    TypePLH entityToDto(TypePLHEntity entity);

    @AfterMapping
    default void orderFils(@MappingTarget TypePLH dto) {
        if (CollectionUtils.isNotEmpty(dto.getFils())) {
            dto.getFils().sort(Comparator.comparing(TypePLH::getOrder));
            dto.getFils().forEach(this::orderFils);
        }
    }
}
