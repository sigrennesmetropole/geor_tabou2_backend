package rm.tabou2.service.helper;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AbstractFicheHelper {


	/**
	 * Fonction mise à disposition pour permettre la conversion d'une date en string depuis les template
	 * @param date LocalDateTime à convertir
	 * @param format Le format de sortie (par exemple : dd/MM/yyyy)
	 * @return La date formatée selon le format saisi
	 */
	public String convertDate(LocalDateTime date, String format) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return date.format(DateTimeFormatter.ofPattern(format));
	}
}
