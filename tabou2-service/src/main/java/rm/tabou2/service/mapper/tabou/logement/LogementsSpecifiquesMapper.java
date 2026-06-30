package rm.tabou2.service.mapper.tabou.logement;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.LogementsSpecifiques;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.logement.LogementsSpecifiquesEntity;

@Mapper(componentModel = "spring", uses = {TypeAccessionLogementMapper.class, LogementSpecifiqueMapper.class})
public interface LogementsSpecifiquesMapper extends AbstractMapper<LogementsSpecifiquesEntity, LogementsSpecifiques> {

    @Override
    @Mapping(target = "logements", ignore = true)
    @Mapping(target = "typeAccessionLogement", ignore = true)
    LogementsSpecifiquesEntity dtoToEntity(LogementsSpecifiques dto);

    @Override
    @Mapping(target = "logements", ignore = true)
    @Mapping(target = "typeAccessionLogement", ignore = true)
    void dtoToEntity(LogementsSpecifiques dto, @MappingTarget LogementsSpecifiquesEntity entity);
}
