package rm.tabou2.service.mapper.tabou.programme;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.Programmation;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.programme.ProgrammationEntity;

@Mapper(componentModel = "spring")
public interface ProgrammationMapper extends AbstractMapper<ProgrammationEntity, Programmation> {

}
