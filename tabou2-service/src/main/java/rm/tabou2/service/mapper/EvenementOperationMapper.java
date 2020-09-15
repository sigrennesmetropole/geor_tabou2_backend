package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.storage.tabou.entity.EvenementOperationEntity;

@Mapper(componentModel = "spring")
public interface EvenementOperationMapper extends AbstractMapper<EvenementOperationEntity, Evenement> {
}
