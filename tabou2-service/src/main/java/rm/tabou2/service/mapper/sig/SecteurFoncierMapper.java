package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.SecteurFoncier;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

@Mapper(componentModel = "spring")
public interface SecteurFoncierMapper extends AbstractMapper<SecteurFoncierEntity, SecteurFoncier> {
}
