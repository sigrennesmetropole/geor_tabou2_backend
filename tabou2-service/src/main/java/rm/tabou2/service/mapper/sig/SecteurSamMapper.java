package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.SecteurSam;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.SecteurSamEntity;

@Mapper(componentModel = "spring")
public interface SecteurSamMapper extends AbstractMapper<SecteurSamEntity, SecteurSam> {
}
