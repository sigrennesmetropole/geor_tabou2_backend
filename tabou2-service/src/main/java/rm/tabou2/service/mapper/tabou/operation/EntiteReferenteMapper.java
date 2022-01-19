package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.EntiteReferenteEntity;
import rm.tabou2.service.dto.EntiteReferente;

@Mapper(componentModel = "spring")
public interface EntiteReferenteMapper extends AbstractMapper<EntiteReferenteEntity, EntiteReferente> {
}
