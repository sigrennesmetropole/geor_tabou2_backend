package rm.tabou2.service.alfresco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import org.springframework.web.util.UriComponentsBuilder;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.helper.AlfrescoAuthenticationHelper;
import rm.tabou2.service.st.generator.model.DocumentContent;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class AlfrescoServiceImpl implements AlfrescoService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";
    public static final String DOWNLOAD_URI_END = "/content?attachment=true";

    @Autowired
    private AlfrescoAuthenticationHelper alfrescoAuthenticationHelper;

    @Value("${temporary.directory}")
    private String temporaryDirectory;

    @Override
    public AlfrescoDocument getDocumentMetadata(String documentId) {

        //Construction de l'uri du document
        UriComponentsBuilder documentUri = UriComponentsBuilder
                .fromUriString(DOCUMENT_START_URI)
                .path(documentId);

        return alfrescoAuthenticationHelper.getAlfrescoWebClient().get()
                .uri(documentUri.toUriString())
                .header(AUTHORIZATION, "Basic " + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }


    @Override
    public DocumentContent downloadDocument(String documentId) {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        String uriForDownload = DOCUMENT_START_URI + documentId + DOWNLOAD_URI_END;

        final Flux<DataBuffer> dataBufferFlux = alfrescoAuthenticationHelper.getAlfrescoWebClient()
                .get()
                .uri(uriForDownload)
                .accept(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL)
                .header(AUTHORIZATION, "Basic " + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve()
                .bodyToFlux(DataBuffer.class);


        final Path path = FileSystems.getDefault().getPath(temporaryDirectory + document.getEntry().getName());
        DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block();

        return new DocumentContent(document.getEntry().getName(), document.getEntry().getContent().getMimeType(), path.toFile());


    }


}
