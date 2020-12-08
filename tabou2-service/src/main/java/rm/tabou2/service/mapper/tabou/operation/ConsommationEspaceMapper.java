package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.ConsommationEspace;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.ConsommationEspaceEntity;

@Mapper(componentModel = "spring")
public interface ConsommationEspaceMapper extends AbstractMapper<ConsommationEspaceEntity, ConsommationEspace> {

    @Named("dtoToNewConsommationEspaceEntity")
    ConsommationEspaceEntity dtoToEntity(ConsommationEspace consommationEspace);
}
