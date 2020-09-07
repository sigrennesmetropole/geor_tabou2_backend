package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.storage.tabou.entity.NatureEntity;

@Mapper(componentModel = "spring")
public interface NatureMapper extends AbstractMapper<NatureEntity, Nature> {


}
