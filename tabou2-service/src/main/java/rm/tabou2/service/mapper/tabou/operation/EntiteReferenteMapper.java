package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;

import rm.tabou2.service.dto.EntiteReferente;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.EntiteReferenteEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface EntiteReferenteMapper extends AbstractMapper<EntiteReferenteEntity, EntiteReferente> {
}
