package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Mapper(componentModel = "spring")
public interface ProgrammeMapper extends AbstractMapper<ProgrammeEntity, Programme> {
}
