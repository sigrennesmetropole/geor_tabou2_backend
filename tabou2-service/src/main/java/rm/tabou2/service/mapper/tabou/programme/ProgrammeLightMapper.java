package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.ProgrammeLight;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Mapper(componentModel = "spring", uses = {EtapeProgrammeMapper.class})
public interface ProgrammeLightMapper extends AbstractMapper<ProgrammeEntity, ProgrammeLight> {

    @Mapping(source = "etapeProgramme.libelle", target = "etape")
    @Mapping(source = "nbLogements", target = "logements")
    @Mapping(source = "adsDatePrevu", target = "dateLiv")
    ProgrammeLight entityToDto(ProgrammeEntity entity);

}
