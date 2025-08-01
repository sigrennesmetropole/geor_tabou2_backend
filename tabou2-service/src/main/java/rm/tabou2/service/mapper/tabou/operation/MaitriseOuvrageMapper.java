package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import rm.tabou2.service.dto.MaitriseOuvrage;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.MaitriseOuvrageEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface MaitriseOuvrageMapper extends AbstractMapper<MaitriseOuvrageEntity, MaitriseOuvrage> {

    @Named("dtoToNewMaitriseOuvrageEntity")
    MaitriseOuvrageEntity dtoToEntity(MaitriseOuvrage maitriseOuvrage);
}
