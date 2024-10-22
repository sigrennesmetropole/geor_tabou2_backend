package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;

import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EvenementProgrammeMapper extends AbstractMapper<EvenementProgrammeEntity, Evenement> {


}
