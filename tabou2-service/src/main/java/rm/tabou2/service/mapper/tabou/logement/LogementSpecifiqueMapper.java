package rm.tabou2.service.mapper.tabou.logement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.LogementSpecifique;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.logement.LogementSpecifiqueEntity;

@Mapper(componentModel = "spring", uses = {TypeLogementMapper.class})
public interface LogementSpecifiqueMapper extends AbstractMapper<LogementSpecifiqueEntity, LogementSpecifique> {

    @Override
    @Mapping(target = "typeLogement", ignore = true)
    LogementSpecifiqueEntity dtoToEntity(LogementSpecifique dto);

    @Override
    @Mapping(target = "typeLogement", ignore = true)
    void dtoToEntity(LogementSpecifique dto, @MappingTarget LogementSpecifiqueEntity entity);
}
