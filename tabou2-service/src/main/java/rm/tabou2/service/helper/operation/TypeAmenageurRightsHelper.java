package rm.tabou2.service.helper.operation;

import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AbstractRightsHelper;
import rm.tabou2.service.helper.AuthentificationHelper;

@Component
public class TypeAmenageurRightsHelper extends AbstractRightsHelper {
	public TypeAmenageurRightsHelper(AuthentificationHelper authentificationHelper) {
		super(authentificationHelper);
	}
}
