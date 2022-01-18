package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.dto.OperationV1;
import rm.tabou2.service.mapper.AbstractMapper;

@Mapper(componentModel = "spring", uses = {EtapeOperationMapper.class, NatureMapper.class, VocationMapper.class,
        DecisionMapper.class, MaitriseOuvrageMapper.class, ModeAmenagementMapper.class, ConsommationEspaceMapper.class})
public interface OperationV1Mapper extends AbstractMapper<OperationIntermediaire, OperationV1> {

    @Mapping(source = "nbLogementsPrevu", target = "nbLogementPrevu")
    @Mapping(source = "plhLogementsPrevus", target = "plh.logementsPrevus")
    @Mapping(source = "plhLogementsLivres", target = "plh.logementsLivres")
    @Mapping(source = "ql2", target = "scot")
    OperationIntermediaire dtoToEntity(OperationV1 dto);

    @Mapping(source = "nbLogementPrevu", target = "nbLogementsPrevu")
    @Mapping(source = "plh.logementsPrevus", target = "plhLogementsPrevus")
    @Mapping(source = "plh.logementsLivres", target = "plhLogementsLivres")
    @Mapping(source = "scot", target = "ql2")
    OperationV1 entityToDto(OperationIntermediaire operationEntity);

    @Mapping(source = "nbLogementsPrevu", target = "nbLogementPrevu")
    @Mapping(source = "plhLogementsPrevus", target = "plh.logementsPrevus")
    @Mapping(source = "plhLogementsLivres", target = "plh.logementsLivres")
    @Mapping(source = "ql2", target = "scot")
    void dtoToEntity(OperationV1 dto, @MappingTarget OperationIntermediaire operationEntity);
}
