package rm.tabou2.facade.controller.tabou.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import rm.tabou2.facade.api.ConfigurationApi;
import rm.tabou2.service.st.ConfigurationService;
import rm.tabou2.service.dto.ConfigurationApp;

@Controller
public class ConfigApiController implements ConfigurationApi {

    @Autowired
    ConfigurationService configurationService;

    @Override
    public ResponseEntity<ConfigurationApp> getConfiguration(Long id) throws Exception {
        return new ResponseEntity<>(configurationService.getConfiguration(id), HttpStatus.OK);
    }
}
