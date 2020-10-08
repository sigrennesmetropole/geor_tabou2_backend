package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;

@Mapper(componentModel = "spring")
public interface NatureMapper extends AbstractMapper<NatureEntity, Nature> {


}
