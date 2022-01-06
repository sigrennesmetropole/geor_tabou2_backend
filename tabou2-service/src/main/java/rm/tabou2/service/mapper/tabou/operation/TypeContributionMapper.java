package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import rm.tabou2.service.dto.TypeContribution;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.storage.tabou.entity.operation.TypeContributionEntity;

@Mapper(componentModel = "spring")
public interface TypeContributionMapper extends AbstractMapper<TypeContributionEntity, TypeContribution> {
}
