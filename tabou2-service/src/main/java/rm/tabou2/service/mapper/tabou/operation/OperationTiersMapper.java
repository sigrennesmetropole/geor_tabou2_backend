package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rm.tabou2.service.dto.TiersAmenagement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.OperationTiersEntity;

@Mapper(componentModel = "spring")
public interface OperationTiersMapper extends AbstractMapper<OperationTiersEntity, TiersAmenagement> {

    @Mapping(source = "typeTiers.libelle", target = "libelle")
    @Mapping(source = "tiers.nom", target = "nom")
    TiersAmenagement entityToDto(OperationTiersEntity tiersAmenagement);

}
