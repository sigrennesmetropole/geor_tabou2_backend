package rm.tabou2.service.mapper.tabou.evenement;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

@Mapper(componentModel = "spring")
public interface TypeEvenementMapper extends AbstractMapper<TypeEvenementEntity, TypeEvenement> {

}
