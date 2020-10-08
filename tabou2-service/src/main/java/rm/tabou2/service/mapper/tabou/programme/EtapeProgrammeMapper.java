package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EtapeProgrammeMapper extends AbstractMapper<EtapeProgrammeEntity, Etape> {

}
