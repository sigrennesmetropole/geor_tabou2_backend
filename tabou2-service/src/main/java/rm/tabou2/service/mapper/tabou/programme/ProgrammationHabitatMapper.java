package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.ProgrammationHabitat;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.tabou.logement.LogementsSpecifiquesMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammationHabitatEntity;

@Mapper(componentModel = "spring", uses = {LogementsSpecifiquesMapper.class})
public interface ProgrammationHabitatMapper extends AbstractMapper<ProgrammationHabitatEntity, ProgrammationHabitat> {

    @Override
    @Mapping(target = "logementsSpecifiques", ignore = true)
    ProgrammationHabitatEntity dtoToEntity(ProgrammationHabitat dto);

    @Override
    @Mapping(target = "logementsSpecifiques", ignore = true)
    void dtoToEntity(ProgrammationHabitat dto, @MappingTarget ProgrammationHabitatEntity entity);
}
