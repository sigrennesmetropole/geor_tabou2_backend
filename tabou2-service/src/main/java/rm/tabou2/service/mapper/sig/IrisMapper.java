package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.IrisEntity;

@Mapper(componentModel = "spring")
public interface IrisMapper extends AbstractMapper<IrisEntity, Iris> {

}
