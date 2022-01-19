package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.dto.OutilFoncier;
import rm.tabou2.storage.tabou.entity.operation.OutilFoncierEntity;

@Mapper(componentModel = "spring")
public interface OutilsFonciersMapper extends AbstractMapper<OutilFoncierEntity, OutilFoncier>{

}
