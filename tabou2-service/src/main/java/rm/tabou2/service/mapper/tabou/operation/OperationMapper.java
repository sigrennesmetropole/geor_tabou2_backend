package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Mapper(componentModel = "spring", uses = {EtapeOperationMapper.class, NatureMapper.class, VocationMapper.class,
        DecisionMapper.class, MaitriseOuvrageMapper.class, ModeAmenagementMapper.class, ConsommationEspaceMapper.class})
public interface OperationMapper extends AbstractMapper<OperationEntity, Operation> {

    @Mapping(source = "etape", target = "etapeOperation")
    OperationEntity dtoToEntity(Operation dto);

    @Mapping(source = "etapeOperation", target = "etape")
    Operation entityToDto(OperationEntity operationEntity);

    @Mapping(source = "etape", target = "etapeOperation", qualifiedByName = "dtoToNewEtapeOperationEntity")
    @Mapping(target = "nature", qualifiedByName = "dtoToNewNatureEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "vocation", qualifiedByName = "dtoToNewVocationEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "decision", qualifiedByName = "dtoToNewDecisionEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "maitriseOuvrage", qualifiedByName = "dtoToNewMaitriseOuvrageEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "modeAmenagement", qualifiedByName = "dtoToNewModeAmenagementEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "consommationEspace", qualifiedByName = "dtoToNewConsommationEspaceEntity",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "secteur",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void dtoToEntity(Operation dto, @MappingTarget OperationEntity operationEntity);
}
