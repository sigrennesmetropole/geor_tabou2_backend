package rm.tabou2.service.mapper;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;

@Mapper(componentModel = "spring")
public interface PermisConstuireMapper extends AbstractMapper<PermisConstruireEntity, PermisConstruire> {
}
