package rm.tabou2.service.helper;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HeaderValueDecoderTest {

    @Test
    void decodeBase64() {
        String encoded = "{base64}" + Base64.getEncoder().encodeToString("Rennes Métropole".getBytes(StandardCharsets.UTF_8));
        assertEquals("Rennes Métropole", HeaderValueDecoder.decode(encoded));
    }

    @Test
    void decodePlain() {
        assertEquals("Bruz", HeaderValueDecoder.decode(" Bruz "));
    }

    @Test
    void decodeUtf8ReadAsLatin1() {
        String mojibake = new String("Saint-Grégoire".getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        assertEquals("Saint-Grégoire", HeaderValueDecoder.decode(mojibake));
    }

    @Test
    void decodeLatin1StaysUnchanged() {
        assertEquals("Orgères", HeaderValueDecoder.decode("Orgères"));
    }

    @Test
    void decodeInvalidOrEmpty() {
        assertNull(HeaderValueDecoder.decode("{base64}%%%"));
        assertNull(HeaderValueDecoder.decode(" "));
        assertNull(HeaderValueDecoder.decode(null));
    }
}
