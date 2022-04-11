package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.*;
import rm.tabou2.service.bean.tabou.operation.OperationIntermediaire;
import rm.tabou2.service.helper.operation.OperationEmpriseHelper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;

@Mapper(componentModel = "spring", uses = {EtapeOperationMapper.class, NatureMapper.class, VocationMapper.class,
        DecisionMapper.class, MaitriseOuvrageMapper.class, ModeAmenagementMapper.class, ConsommationEspaceMapper.class, OperationEmpriseHelper.class})
public interface OperationMapper extends AbstractMapper<OperationEntity, OperationIntermediaire> {

    @Mapping(target = "etapeOperation", ignore = true)
    @Mapping(target = "nature", ignore = true)
    @Mapping(target = "vocation", ignore = true)
    @Mapping(target = "vocationZa", ignore = true)
    @Mapping(target = "decision", ignore = true)
    @Mapping(target = "maitriseOuvrage", ignore = true)
    @Mapping(target = "modeAmenagement", ignore = true)
    @Mapping(target = "consommationEspace", ignore = true)
    @Mapping(target = "concertation", ignore = true)
    @Mapping(target = "entiteReferente", ignore = true)
    @Mapping(target = "outilFoncier", ignore = true)
    @Mapping(target = "typeOccupation", ignore = true)
    @Override
    OperationEntity dtoToEntity(OperationIntermediaire dto);

    @Mapping(source = "etapeOperation", target = "etape")
    @Mapping(source = ".", target = "idEmprise", qualifiedByName = "getIdEmpriseOperation")
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
    @Mapping(target = "programmes", ignore = true)
    @Mapping(target = "acteurs", ignore = true)
    @Mapping(target = "actions", ignore = true)
    @Mapping(target = "amenageurs", ignore = true)
    @Mapping(target = "contributions", ignore = true)
    @Mapping(target = "descriptionsFoncier", ignore = true)
    @Mapping(target = "financements", ignore = true)
    @Mapping(target = "informationsProgrammation", ignore = true)
    @Mapping(target = "operationsTiers", ignore = true)
    @Mapping(target = "evenements", ignore = true)
    @Mapping(target = "typeOccupation", ignore = true)
    @Mapping(target = "concertation", ignore = true)
    @Mapping(target = "entiteReferente", ignore = true)
    @Mapping(target = "outilFoncier", ignore = true)
    @Mapping(target = "secteur",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Override
    void dtoToEntity(OperationIntermediaire dto, @MappingTarget OperationEntity operationEntity);

}
