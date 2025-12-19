/**
 * 
 */
package rm.tabou2.facade.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

/**
 * @author FNI18300
 *
 */
public class StringToOfssetDateTimeConverter implements Converter<String, OffsetDateTime> {

    private static final String FAILED_TO_PARSE_DATE = "Failed to parse date :{}";

    private static final Logger LOGGER = LoggerFactory.getLogger(StringToOfssetDateTimeConverter.class);

    private static final DateTimeFormatter[] FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    @Override
    public OffsetDateTime convert(@Nullable String source) {
        if (source == null) {
            return null;
        }
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return OffsetDateTime.parse(source, formatter);
            } catch (Exception e) {
                // ignore and try next
            }
        }
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(source, formatter).atTime(LocalTime.MIDNIGHT).atOffset(ZoneOffset.UTC);
            } catch (Exception e) {
                // ignore and try next
            }
        }
        LOGGER.warn(FAILED_TO_PARSE_DATE, source);
        return null;
    }

}
