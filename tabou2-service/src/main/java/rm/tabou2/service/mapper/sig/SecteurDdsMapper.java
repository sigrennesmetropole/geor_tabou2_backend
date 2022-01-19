package rm.tabou2.service.mapper.sig;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.SecteurDds;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

@Mapper(componentModel = "spring")
public interface SecteurDdsMapper extends AbstractMapper<SecteurDdsEntity, SecteurDds> {

}
