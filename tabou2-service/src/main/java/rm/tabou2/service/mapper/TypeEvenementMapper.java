package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.storage.tabou.entity.TypeEvenementEntity;

@Mapper(componentModel = "spring")
public interface TypeEvenementMapper extends AbstractMapper<TypeEvenementEntity, TypeEvenement> {

}
