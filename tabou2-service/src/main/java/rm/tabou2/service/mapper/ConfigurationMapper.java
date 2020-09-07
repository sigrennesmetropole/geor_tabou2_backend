package rm.tabou2.service.mapper;


import org.mapstruct.Mapper;
import rm.tabou2.storage.tabou.entity.Configuration;
import rm.tabou2.service.dto.ConfigurationApp;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper extends AbstractMapper<Configuration, ConfigurationApp> {
}

