package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.programme.ProgrammeEmpriseHelper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Mapper(componentModel = "spring", uses = {EtapeProgrammeMapper.class, ProgrammeEmpriseHelper.class})
public interface ProgrammeMapper extends AbstractMapper<ProgrammeEntity, Programme> {

    @Mapping(source = "etape", target = "etapeProgramme")
    @Mapping(source = "logementsTotal", target = "nbLogements")
    @Mapping(target = "operation", ignore = true)
    @Mapping(source = "livraisonDate", target = "dateLivraison")
    ProgrammeEntity dtoToEntity(Programme dto);

    @Mapping(source = "etapeProgramme", target = "etape")
    @Mapping(source = "nbLogements", target = "logementsTotal")
    @Mapping(source = "operation.id", target = "operationId")
    @Mapping(source = "dateLivraison", target = "livraisonDate")
    @Mapping(source = ".", target = "idEmprise", qualifiedByName = "getIdEmpriseProgramme")
    Programme entityToDto(ProgrammeEntity entity);

    @Mapping(source = "etape", target = "etapeProgramme", qualifiedByName = "dtoToNewEntity")
    @Mapping(source = "logementsTotal", target = "nbLogements")
    @Mapping(source = "livraisonDate", target = "dateLivraison")
    @Mapping(target = "operation", ignore = true)
    void dtoToEntity(Programme dto, @MappingTarget ProgrammeEntity entity);

}
