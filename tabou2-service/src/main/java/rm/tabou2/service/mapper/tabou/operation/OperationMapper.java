package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Mapper(componentModel = "spring", uses = {EtapeOperationMapper.class, NatureMapper.class, VocationMapper.class,
        DecisionMapper.class, MaitriseOuvrageMapper.class, ModeAmenagementMapper.class, ConsommationEspaceMapper.class})
public interface OperationMapper extends AbstractMapper<OperationEntity, OperationIntermediaire> {

    @Mapping(target = "etapeOperation", ignore = true)
    @Mapping(target = "nature", ignore = true)
    @Mapping(target = "vocation", ignore = true)
    @Mapping(target = "vocationZa", ignore = true)
    @Mapping(target = "decision", ignore = true)
    @Mapping(target = "maitriseOuvrage", ignore = true)
    @Mapping(target = "modeAmenagement", ignore = true)
    @Mapping(target = "consommationEspace", ignore = true)
    @Override
    OperationEntity dtoToEntity(OperationIntermediaire dto);

    @Mapping(source = "etapeOperation", target = "etape")
    @Override
    OperationIntermediaire entityToDto(OperationEntity operationEntity);

    @Mapping(target = "etapeOperation", ignore = true)
    @Mapping(target = "nature", ignore = true)
    @Mapping(target = "vocation", ignore = true)
    @Mapping(target = "vocationZa", ignore = true)
    @Mapping(target = "decision", ignore = true)
    @Mapping(target = "maitriseOuvrage", ignore = true)
    @Mapping(target = "modeAmenagement", ignore = true)
    @Mapping(target = "consommationEspace", ignore = true)
    @Mapping(target = "secteur",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Override
    void dtoToEntity(OperationIntermediaire dto, @MappingTarget OperationEntity operationEntity);
}
