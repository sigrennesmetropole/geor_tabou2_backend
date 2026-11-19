package rm.tabou2.service.sig;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Réponse GeoServer relayée telle quelle (statut, type de contenu, flux). À fermer après lecture : sans lecture du
 * flux, la fermeture libère la connexion.
 */
public class GeoserverStream implements Closeable {

    private final int status;

    private final String contentType;

    private final Flux<DataBuffer> body;

    private final AtomicBoolean consumed = new AtomicBoolean();

    public GeoserverStream(int status, String contentType, Flux<DataBuffer> body) {
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * Écrit le corps de la réponse, sans le charger en mémoire.
     */
    public void writeTo(OutputStream out) throws IOException {
        if (!consumed.compareAndSet(false, true)) {
            throw new IllegalStateException("Le flux GeoServer a déjà été lu");
        }
        try {
            // L'écriture est bloquante : hors du thread d'entrée-sortie de Netty
            DataBufferUtils.write(body.publishOn(Schedulers.boundedElastic()), out)
                    .doOnNext(DataBufferUtils::release).blockLast();
        } catch (RuntimeException e) {
            Throwable cause = Exceptions.unwrap(e);
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new IOException("Lecture de la réponse GeoServer interrompue", cause);
        }
    }

    @Override
    public void close() {
        if (consumed.compareAndSet(false, true)) {
            body.subscribe(DataBufferUtils.releaseConsumer()).dispose();
        }
    }
}
