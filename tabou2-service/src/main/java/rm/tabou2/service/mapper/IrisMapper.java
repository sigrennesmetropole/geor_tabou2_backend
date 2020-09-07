package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Iris;
import rm.tabou2.storage.tabou.entity.administratif.IrisEntity;

@Mapper(componentModel = "spring")
public interface IrisMapper extends AbstractMapper<IrisEntity, Iris> {

}
