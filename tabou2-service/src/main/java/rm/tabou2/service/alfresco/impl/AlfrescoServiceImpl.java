package rm.tabou2.service.alfresco.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import org.springframework.web.util.UriComponentsBuilder;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoEmptyNode;
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
    private static final String CONTENT_TYPE = "cm:content";
    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";
    private static final String DOWNLOAD_URI_END = "/content?attachment=true";
    private static final String DELETE_URI_END = "?permanent=false";
    private static final String CONTENT_URI = "/content";
    private static final String CHILDREN_URI = "/children";

    @Value("${alfresco.tabou.folder.node.id}")
    private String tabouNodeId;

    private static final String ACCESS_RIGHTS_EXCEPTION = "L'utilisateur n'a pas les droits de récupérer le document id=";

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
    public void updateDocumentContent(AlfrescoTabouType objectType, long objectId, String documentId, MultipartFile file) throws AppServiceException {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        //Vérification de la cohérence
        if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
                document.getEntry().getProperties().getTabouId() != objectId) {
            throw new AccessDeniedException(ACCESS_RIGHTS_EXCEPTION + documentId);
        }

        //Construction de l'uri du document
        UriComponentsBuilder documentUri = UriComponentsBuilder
                .fromUriString(DOCUMENT_START_URI)
                .path(documentId).path(CONTENT_URI);


        alfrescoAuthenticationHelper.getAlfrescoWebClient().put()
                .uri(documentUri.toUriString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromResource(getResourceFromFile(file)))
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }


    /**
     * Création d'une resource à partir d'un multipart file.
     *
     * @param file fichier
     * @return resource
     * @throws AppServiceException exception levée si erreur lors de la lecture du fichier
     */
    private Resource getResourceFromFile(MultipartFile file) throws AppServiceException {

        try {
            return new ByteArrayResource(file.getBytes());
        } catch (IOException e) {
            throw new AppServiceException(e.getMessage(), e);
        }
    }


    @Override
    public DocumentContent downloadDocument(AlfrescoTabouType objectType, long objectId, String documentId) throws AppServiceException {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        //Vérification de la cohérence
        if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
                document.getEntry().getProperties().getTabouId() != objectId) {
            throw new AccessDeniedException(ACCESS_RIGHTS_EXCEPTION + documentId);
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
            throw new AppServiceException("Erreur lors de la génération du fichier temporaire", e);
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
            throw new AccessDeniedException(ACCESS_RIGHTS_EXCEPTION + documentId);
        }

        alfrescoAuthenticationHelper.getAlfrescoWebClient().delete()
                .uri(documentUri)
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(Void.class).block();

    }

    @Override
    public AlfrescoDocument addDocument(String nom, String libelle, AlfrescoTabouType objectType, long objectId, MultipartFile file) throws AppServiceException {

        //Construction de l'uri du document
        String documentUri = DOCUMENT_START_URI + tabouNodeId + CHILDREN_URI;


        //Création du noeud tabou
        AlfrescoEmptyNode emptyNode = new AlfrescoEmptyNode();
        emptyNode.setName(nom);
        emptyNode.setNodeType(CONTENT_TYPE);

        AlfrescoDocument document = alfrescoAuthenticationHelper.getAlfrescoWebClient().post()
                .uri(documentUri)
                .body(BodyInserters.fromValue(emptyNode))
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setLibelle(libelle);

        //Mise à jour des métadonnées
        AlfrescoDocument updatedDocument = updateDocumentMetadata(objectType, objectId, document.getEntry().getId(), documentMetadata, true);

        //Mise à jour du contenu
        updateDocumentContent(objectType, objectId, document.getEntry().getId(), file);

        return updatedDocument;


    }

    @Override
    public AlfrescoDocument updateDocumentMetadata(AlfrescoTabouType objectType, long objectId, String documentId, DocumentMetadata documentMetadata, boolean isNewDocument) {

        AlfrescoDocument document = getDocumentMetadata(documentId);

        AlfrescoProperties properties = new AlfrescoProperties();

        //Si le document est nouveau, on lui ajoute l'id tabou et son type
        if (isNewDocument) {
            properties.setTabouId(objectId);
            properties.setObjetTabou(objectType.toString());
        } else {
            //Vérification de la cohérence
            if (!objectType.toString().equals(document.getEntry().getProperties().getObjetTabou()) ||
                    document.getEntry().getProperties().getTabouId() != objectId) {
                throw new AccessDeniedException(ACCESS_RIGHTS_EXCEPTION + documentId);
            }
        }

        //Construction de l'uri du document
        UriComponentsBuilder documentUri = UriComponentsBuilder
                .fromUriString(DOCUMENT_START_URI)
                .path(documentId);

        properties.setCmTitle(documentMetadata.getNom());
        properties.setLibelleTypeDocument(documentMetadata.getLibelle());
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
