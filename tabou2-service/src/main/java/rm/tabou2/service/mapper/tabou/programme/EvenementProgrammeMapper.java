package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.EvenementProgrammeEntity;

@Mapper(componentModel = "spring")
public interface EvenementProgrammeMapper extends AbstractMapper<EvenementProgrammeEntity, Evenement> {

    @Mapping(source = "typeEvenement.id", target = "idType")
    Evenement entityToDto(EvenementProgrammeEntity evenementProgrammeEntity);
}
