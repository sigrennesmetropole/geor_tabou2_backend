package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EvenementOperationEntity;

@Mapper(componentModel = "spring")
public interface EvenementOperationMapper extends AbstractMapper<EvenementOperationEntity, Evenement> {
}
