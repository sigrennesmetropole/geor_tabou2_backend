package rm.tabou2.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Décodage des valeurs d'en-têtes sec-* posées par la passerelle geOrchestra.
 */
public final class HeaderValueDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderValueDecoder.class);

    private static final String BASE64_PREFIX = "{base64}";

    private HeaderValueDecoder() {
    }

    /**
     * Décode une valeur d'en-tête : la passerelle préfixe par {base64} les valeurs non ASCII.
     * Sans préfixe, une valeur UTF-8 relue en ISO-8859-1 par le conteneur est rétablie.
     *
     * @param value valeur brute de l'en-tête
     * @return la valeur décodée, ou null si absente, vide ou non décodable
     */
    public static String decode(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String result = value.trim();
        if (result.regionMatches(true, 0, BASE64_PREFIX, 0, BASE64_PREFIX.length())) {
            try {
                return new String(Base64.getDecoder().decode(result.substring(BASE64_PREFIX.length()).trim()),
                        StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Valeur d'en-tête préfixée {base64} mais non décodable");
                return null;
            }
        }
        try {
            return StandardCharsets.UTF_8.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(result.getBytes(StandardCharsets.ISO_8859_1))).toString();
        } catch (CharacterCodingException e) {
            return result;
        }
    }
}
