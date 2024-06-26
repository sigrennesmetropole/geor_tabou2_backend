package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import rm.tabou2.service.dto.Etape;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EtapeProgrammeMapper extends AbstractMapper<EtapeProgrammeEntity, Etape> {

    @Named("dtoToNewEntity")
    EtapeProgrammeEntity dtoToEntity(Etape etape);

}
