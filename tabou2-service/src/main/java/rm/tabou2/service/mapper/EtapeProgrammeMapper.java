package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.storage.tabou.entity.EtapeProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EtapeProgrammeMapper extends AbstractMapper<EtapeProgrammeEntity, Etape> {

}
