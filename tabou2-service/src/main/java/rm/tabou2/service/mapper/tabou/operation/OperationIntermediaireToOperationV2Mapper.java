package rm.tabou2.service.mapper.tabou.operation;


import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.dto.Operation;

@Mapper(componentModel = "spring", uses = {EtapeOperationMapper.class, NatureMapper.class, VocationMapper.class,
        DecisionMapper.class, MaitriseOuvrageMapper.class, ModeAmenagementMapper.class, ConsommationEspaceMapper.class})
public interface OperationIntermediaireToOperationV2Mapper extends AbstractMapper<OperationIntermediaire, Operation> {

}
