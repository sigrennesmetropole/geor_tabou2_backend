/**
 * 
 */
package rm.tabou2.facade.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

import lombok.RequiredArgsConstructor;

/**
 * @author FNI18300
 *
 */
@Configuration
@RequiredArgsConstructor
public class FacadeConfig {

	private final GenericConversionService applicationConversionService;

	@PostConstruct
	public void jsonCustomizer() {
		applicationConversionService.addConverter(new StringToDateConverter());
	}
}
