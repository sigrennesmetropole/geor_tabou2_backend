package rm.tabou2.service.alfresco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import org.springframework.web.util.UriComponentsBuilder;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoMetadata;
import rm.tabou2.service.alfresco.dto.AlfrescoProperties;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.alfresco.helper.AlfrescoAuthenticationHelper;
import rm.tabou2.service.dto.DocumentMetadata;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class AlfrescoServiceImpl implements AlfrescoService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC_AUTHENTIFICATION = "Basic ";
    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";
    public static final String DOWNLOAD_URI_END = "/content?attachment=true";
    public static final String DELETE_URI_END = "?permanent=false";

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
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }


    @Override
    public DocumentContent downloadDocument(AlfrescoTabouType objectType, long objectId, String documentId) throws AppServiceException {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        //Vérification de la cohérence
        if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
         document.getEntry().getProperties().getTabouId() != objectId) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le document id=" + documentId);
        }

        String uriForDownload = DOCUMENT_START_URI + documentId + DOWNLOAD_URI_END;

        final Flux<DataBuffer> dataBufferFlux = alfrescoAuthenticationHelper.getAlfrescoWebClient()
                .get()
                .uri(uriForDownload)
                .accept(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL)
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve()
                .bodyToFlux(DataBuffer.class);

        try {
            File tempFile = File.createTempFile(document.getEntry().getName(), "", new File(temporaryDirectory));
            final Path path = FileSystems.getDefault().getPath(tempFile.getPath());
            DataBufferUtils.write(dataBufferFlux, path, StandardOpenOption.CREATE).block();

            return new DocumentContent(document.getEntry().getName(), document.getEntry().getContent().getMimeType(), path.toFile());
        } catch (IOException e) {
            throw new AppServiceException("erreur lors de la génération du fichier temporaire", e);
        }

    }

    @Override
    public void deleteDocument(AlfrescoTabouType objectType, long objectId, String documentId) {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        //Construction de l'uri du document
        String documentUri = DOCUMENT_START_URI + documentId + DELETE_URI_END;

        //Vérification de la cohérence
        if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
                document.getEntry().getProperties().getTabouId() != objectId) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le document id=" + documentId);
        }

        alfrescoAuthenticationHelper.getAlfrescoWebClient().delete()
                .uri(documentUri)
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(Void.class).block();

    }

    @Override
    public AlfrescoDocument updateDocumentMetadata(AlfrescoTabouType objectType, long objectId, String documentId, DocumentMetadata documentMetadata) {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        //Vérification de la cohérence
        if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
                document.getEntry().getProperties().getTabouId() != objectId) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le document id=" + documentId);
        }

        //Construction de l'uri du document
        UriComponentsBuilder documentUri = UriComponentsBuilder
                .fromUriString(DOCUMENT_START_URI)
                .path(documentId);


        AlfrescoProperties properties = new AlfrescoProperties();
        properties.setCmTitle(documentMetadata.getNom());
        properties.setLibelleTypeDocument(documentMetadata.getLibelle());
        //ID : on ne peut pas
        //Type MIME : déterminé par le contenu
        //User et date : automatique
        AlfrescoMetadata alfrescoMetadata = new AlfrescoMetadata();
        alfrescoMetadata.setProperties(properties);
        

        return alfrescoAuthenticationHelper.getAlfrescoWebClient().put()
                .uri(documentUri.toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(alfrescoMetadata))
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }



}
