package rm.tabou2.service.mapper.tabou.financement;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeFinancement;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface TypeFinancementMapper extends AbstractMapper<TypeFinancementEntity, TypeFinancement> {

}
