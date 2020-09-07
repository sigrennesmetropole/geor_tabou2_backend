package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.storage.tabou.entity.ProgrammeEntity;

@Mapper(componentModel = "spring")
public interface ProgrammeMapper extends AbstractMapper<ProgrammeEntity, Programme> {
}
