package rm.tabou2.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import rm.tabou2.storage.tabou.dao.ConfigurationDao;
import rm.tabou2.storage.tabou.entity.Configuration;
import rm.tabou2.service.dto.ConfigurationApp;
import rm.tabou2.service.exception.AppServiceNotFoundException;
import rm.tabou2.service.impl.ConfigurationServiceImpl;
import rm.tabou2.service.mapper.ConfigurationMapper;

public class ConfigurationServiceTest {

    @Mock
    private ConfigurationDao configurationDao;

    @Spy
    private final ConfigurationMapper configurationMapper = Mappers.getMapper(ConfigurationMapper.class);

    @Autowired
    @InjectMocks
    private ConfigurationServiceImpl configurationService;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        // Initilisation de valeur du fichier properties
        ReflectionTestUtils.setField(configurationService, "applicationVersion", APP_VERSION);

    }

    private static final String MSG = "Good Test";
    private static final String APP_VERSION = "V1.0.0.0";


    @Test
    public void getConfigTest() throws AppServiceNotFoundException {

        // Simuler l'appel à configurationDao
        Mockito.when(configurationDao.findConfigurationById(Mockito.anyLong())).thenReturn(this.generateConfiguration());

        // Appel de la méthode à tester
        ConfigurationApp configuration = configurationService.getConfiguration(1);

        // Test
        Assert.assertEquals(configuration.getComment(), MSG);
        Assert.assertEquals(configuration.getVersionApp(), APP_VERSION);
    }

    /**
     * Generation d'un object configuration
     *
     * @return
     */
    private Configuration generateConfiguration() {
        Configuration cf = new Configuration();
        cf.setId(1);
        cf.setComment(MSG);
        return cf;
    }

}