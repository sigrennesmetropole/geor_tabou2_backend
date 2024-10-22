package rm.tabou2.facade.controller.tabou.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rm.tabou2.facade.api.ConfigurationApi;
import rm.tabou2.service.dto.ConfigurationApp;
import rm.tabou2.service.st.configuration.ConfigurationService;

@RestController
@RequiredArgsConstructor
public class ConfigApiController implements ConfigurationApi {

	private final ConfigurationService configurationService;

	@Override
	public ResponseEntity<ConfigurationApp> getConfiguration(Long id) throws Exception {
		return new ResponseEntity<>(configurationService.getConfiguration(id), HttpStatus.OK);
	}
}
