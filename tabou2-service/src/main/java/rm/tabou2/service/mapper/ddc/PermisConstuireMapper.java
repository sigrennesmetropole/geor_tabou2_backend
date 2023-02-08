package rm.tabou2.service.mapper.ddc;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;

@Mapper(componentModel = "spring")
public interface PermisConstuireMapper extends AbstractMapper<PermisConstruireEntity, PermisConstruire> {

    @Mapping(source = "depotDossier", target = "dateDepotDossier")
    @Mapping(source = "decisionDossier", target = "decision")
    PermisConstruireEntity dtoToEntity(PermisConstruire dto);

    @Mapping(source = "depotDossier", target = "dateDepotDossier")
    @Mapping(source = "decisionDossier", target = "decision")
    @Mapping(source = ".", target = "numAds", qualifiedByName = "concatenateAds")
    void dtoToEntity(PermisConstruire dto, @MappingTarget PermisConstruireEntity entity);

    @Mapping(source = "dateDepotDossier", target = "depotDossier")
    @Mapping(source = "decision", target = "decisionDossier")
    @Mapping(source = ".", target = "numAds", qualifiedByName = "concatenateAds")
    PermisConstruire entityToDto(PermisConstruireEntity entity);

    @Named("concatenateAds")
    default String contcatenateAds(PermisConstruireEntity entity){
        String result = entity.getNumAds();
        if(entity.getVersionAds() != null){
            result += "-" + entity.getVersionAds();
        }
        return result;
    }

}
