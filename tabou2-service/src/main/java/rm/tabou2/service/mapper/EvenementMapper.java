package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.storage.tabou.entity.EvenementEntity;

@Mapper(componentModel = "spring")
public interface EvenementMapper extends AbstractMapper<EvenementEntity, Evenement> {
}
