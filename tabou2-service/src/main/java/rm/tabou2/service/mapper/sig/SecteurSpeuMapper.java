package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.SecteurSpeu;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

@Mapper(componentModel = "spring")
public interface SecteurSpeuMapper extends AbstractMapper<SecteurSpeuEntity, SecteurSpeu> {
}
