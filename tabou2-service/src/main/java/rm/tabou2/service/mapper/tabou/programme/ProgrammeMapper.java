package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.tabou.operation.OperationMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Mapper(componentModel = "spring", uses = {EtapeProgrammeMapper.class, OperationMapper.class})
public interface ProgrammeMapper extends AbstractMapper<ProgrammeEntity, Programme> {

    @Mapping(source = "etape", target = "etapeProgramme")
    @Mapping(target = "operation", ignore = true)
    ProgrammeEntity dtoToEntity(Programme dto);

    @Mapping(source = "etapeProgramme", target = "etape")
    @Mapping(source = "operation", target = "operation", qualifiedByName = "entityToDtoNomNature")
    Programme entityToDto(ProgrammeEntity entity);

    @Mapping(source = "etape", target = "etapeProgramme", qualifiedByName = "dtoToNewEntity")
    @Mapping(target = "operation", ignore = true)
    void dtoToEntity(Programme dto, @MappingTarget ProgrammeEntity entity);

}
