/**
 * 
 */
package rm.tabou2.facade.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * @author FNI18300
 *
 */
public class StringToDateConverter implements Converter<String, Date> {

	private static final String FAILED_TO_PARSE_DATE = "Failed to parse date :{}";

	private static final Logger LOGGER = LoggerFactory.getLogger(StringToDateConverter.class);

	private static final List<ThreadLocal<SimpleDateFormat>> FORMATTERS = new ArrayList<>();

	static {
		FORMATTERS.add(ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")));
		FORMATTERS.add(ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")));
		FORMATTERS.add(ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd")));
	}

	@Override
	public Date convert(String source) {
		for (ThreadLocal<SimpleDateFormat> threadLocal : FORMATTERS) {
			try {
				return threadLocal.get().parse(source);
			} catch (ParseException e) {
				// Nothing to avoid warning
			}
		}
		LOGGER.warn(FAILED_TO_PARSE_DATE,source);
		return null;
	}

}
