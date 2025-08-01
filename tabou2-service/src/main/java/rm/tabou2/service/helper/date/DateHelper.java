/**
 * 
 */
package rm.tabou2.service.helper.date;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 */
@Component
public class DateHelper {

	@Value("${tabou2.date-helper.zone-id:Europe/Paris}")
	private String zoneId;
	
	public LocalDateTime now() {
		return LocalDateTime.now(ZoneId.of(zoneId));
	}
	
	public OffsetDateTime nowOffset() {
		return OffsetDateTime.now(ZoneId.of(zoneId));
	}
	
	public LocalDateTime convert(OffsetDateTime offsetDateTime) {
		if (offsetDateTime == null) {
			return null;
		}
		return offsetDateTime.atZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
	}
	
	public OffsetDateTime convertToOffset(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
		return localDateTime.atZone(ZoneId.of(zoneId)).toOffsetDateTime();
	}
}
