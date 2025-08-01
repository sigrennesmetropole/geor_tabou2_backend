/**
 * 
 */
package rm.tabou2.facade.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * @author FNI18300
 *
 */
public class StringToOfssetDateTimeConverter implements Converter<String, OffsetDateTime> {

	private static final String FAILED_TO_PARSE_DATE = "Failed to parse date :{}";

	private static final Logger LOGGER = LoggerFactory.getLogger(StringToOfssetDateTimeConverter.class);

	private static final List<ThreadLocal<DateTimeFormatter>> FORMATTERS = new ArrayList<>();

	static {
		FORMATTERS.add(ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));
		FORMATTERS.add(ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")));
		FORMATTERS.add(ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
		FORMATTERS.add(ThreadLocal.withInitial(() -> DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Override
	public OffsetDateTime convert(String source) {
		for (ThreadLocal<DateTimeFormatter> threadLocal : FORMATTERS) {
			try {
				DateTimeFormatter formatter = threadLocal.get();
				return OffsetDateTime.parse(source, formatter);
			} catch (Exception e) {
				// noting to avoid warning
			}
		}
		for (ThreadLocal<DateTimeFormatter> threadLocal : FORMATTERS) {
			try {
				DateTimeFormatter formatter = threadLocal.get();
				return LocalDate.parse(source, formatter).atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC);
			} catch (Exception e) {
				// noting to avoid warning
			}
		}
		LOGGER.warn(FAILED_TO_PARSE_DATE,source);
		return null;
	}

}
