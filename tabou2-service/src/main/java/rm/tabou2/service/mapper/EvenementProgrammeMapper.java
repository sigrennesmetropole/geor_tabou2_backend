package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.storage.tabou.entity.EvenementProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EvenementProgrammeMapper extends AbstractMapper<EvenementProgrammeEntity, Evenement> {
}
