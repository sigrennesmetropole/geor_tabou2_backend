/**
 * 
 */
package rm.tabou2.service.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Component;

import jakarta.activation.DataHandler;
import lombok.RequiredArgsConstructor;
import rm.tabou2.service.helper.date.DateHelper;

/**
 * 
 */
@Component
@RequiredArgsConstructor
public class LocaDateTimeMapper {

	private final DateHelper dateHelper;

	public LocalDateTime convertUTC(OffsetDateTime offsetDateTime) {
		return dateHelper.convert(offsetDateTime);
	}

	public OffsetDateTime convertToOffset(LocalDateTime localDateTime) {
		return dateHelper.convertToOffset(localDateTime);
	}

}
