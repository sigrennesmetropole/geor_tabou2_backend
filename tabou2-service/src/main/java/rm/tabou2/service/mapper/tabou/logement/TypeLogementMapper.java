package rm.tabou2.service.mapper.tabou.logement;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

@Mapper(componentModel = "spring", uses = {LocaDateTimeMapper.class})
public interface TypeLogementMapper extends AbstractMapper<TypeLogementEntity, TypeLogement> {
}

