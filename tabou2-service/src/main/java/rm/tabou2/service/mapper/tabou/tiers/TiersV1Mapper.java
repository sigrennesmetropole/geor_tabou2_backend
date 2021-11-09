package rm.tabou2.service.mapper.tabou.tiers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.dto.TiersV1;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TiersV1Mapper {

    @Mapping(source="adresse", target ="adresseRue")
    TiersV1 tiersToTiersV1(Tiers tiers);

    @Mapping(source=".", target="adresse")
    Tiers tiersV1ToTiers(TiersV1 tiersV1);

    List<TiersV1> tiersToTiersV1List(List<Tiers> tiersList);

    default Page<TiersV1> tiersToTiersV1Page(Page<Tiers> tiersPage, Pageable pageable){
        return new PageImpl<>(tiersToTiersV1List(tiersPage.getContent()), pageable, tiersPage.getTotalElements());
    }

    default String getAdresse(TiersV1 s) {
        if(StringUtils.isEmpty(s.getAdresseNum()) && StringUtils.isEmpty(s.getAdresseRue())){
            return null;
        }
        StringBuilder result = new StringBuilder();
        if(!StringUtils.isEmpty(s.getAdresseNum())){
            result.append(s.getAdresseNum());
        }
        if(!StringUtils.isEmpty(s.getAdresseNum()) && !StringUtils.isEmpty(s.getAdresseRue())){
            result.append(" ");
        }
        if(!StringUtils.isEmpty(s.getAdresseRue())){
            result.append(s.getAdresseRue());
        }
        return result.toString();
    }
}
