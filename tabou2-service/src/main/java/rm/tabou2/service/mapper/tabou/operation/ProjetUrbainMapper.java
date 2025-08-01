package rm.tabou2.service.mapper.tabou.operation;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import rm.tabou2.service.dto.ProjetUrbain;
import rm.tabou2.service.mapper.AbstractMapper;
import rm.tabou2.service.mapper.LocaDateTimeMapper;
import rm.tabou2.storage.tabou.entity.operation.ProjetUrbainEntity;

@Mapper(componentModel = "spring", uses = { LocaDateTimeMapper.class })
public interface ProjetUrbainMapper extends AbstractMapper<ProjetUrbainEntity, ProjetUrbain> {

    @Named("dtoToNewProjetUrbainEntity")
    ProjetUrbainEntity dtoToEntity(ProjetUrbain projetUrbain);
}
