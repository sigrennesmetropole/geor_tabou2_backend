package rm.tabou2.service.alfresco.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoDocumentRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoEmptyNode;
import rm.tabou2.service.alfresco.dto.AlfrescoMetadata;
import rm.tabou2.service.alfresco.dto.AlfrescoPaging;
import rm.tabou2.service.alfresco.dto.AlfrescoProperties;
import rm.tabou2.service.alfresco.dto.AlfrescoSearchQuery;
import rm.tabou2.service.alfresco.dto.AlfrescoSearchRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoSort;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class AlfrescoServiceImpl implements AlfrescoService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC_AUTHENTIFICATION = "Basic ";
    private static final String CONTENT_TYPE = "cm:content";
    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";
    public static final String DOWNLOAD_URI_END = "/content?attachment=true";
    public static final String DELETE_URI_END = "?permanent=false";
    public static final String SEARCH_URI = "search/versions/1/search";
    private static final String CONTENT_URI = "/content";
    private static final String CHILDREN_URI = "/children";

    public static final String SEARCH_PARAM_PROPERTIES = "properties";
    public static final String SEARCH_PARAM_ID = "=tabou2:id:";
    public static final String SEARCH_PARAM_LIBELLE_TYPE_DOCUMENT = "=tabou2:libelleTypeDocument:";
    public static final String SEARCH_PARAM_OBJET = "=tabou2:objet:";
    public static final String SEARCH_PARAM_MIME_TYPE = "=cm:content.mimetype:";
    public static final String SEARCH_PARAM_NAME = "=cm:name:";
    public static final String ALFRESCO_SEARCH_CRITERIA_TYPE = "FIELD";
    public static final String AND = " AND ";

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
    public AlfrescoDocumentRoot searchDocuments(AlfrescoTabouType objectType, long objectId, String nom, String libelleTypeDocument, String typeMime, Pageable pageable) {

        //Construction de l'uri du document
        UriComponentsBuilder searchUri = UriComponentsBuilder
                .fromUriString(SEARCH_URI);


        AlfrescoSearchRoot searchRoot = new AlfrescoSearchRoot();
        AlfrescoSearchQuery searchQuery = new AlfrescoSearchQuery();

        //Dans tous les cas, on filtre sur le type d'objet
        StringBuilder query = new StringBuilder();
        query.append(SEARCH_PARAM_OBJET).append(objectType);
        if (!StringUtils.isEmpty(nom)) {
            query.append(AND);
            query.append(SEARCH_PARAM_NAME).append("\"").append(nom).append("\"");
        }
        if (objectId > 0) {
            query.append(AND);
            query.append(SEARCH_PARAM_ID).append(objectId);
        }
        if (!StringUtils.isEmpty(libelleTypeDocument)) {
            query.append(AND);
            query.append(SEARCH_PARAM_LIBELLE_TYPE_DOCUMENT).append(libelleTypeDocument);
        }
        if (!StringUtils.isEmpty(typeMime)) {
            query.append(AND);
            query.append(SEARCH_PARAM_MIME_TYPE).append(typeMime);
        }
        searchQuery.setQuery(query.toString());
        searchRoot.setQuery(searchQuery);

        //On veut que la requête nous retourne les properties
        ArrayList<String> includes = new ArrayList<>();
        includes.add(SEARCH_PARAM_PROPERTIES);
        searchRoot.setInclude(includes);

        //Paging
        AlfrescoPaging paging = new AlfrescoPaging();
        paging.setMaxItems(pageable.getPageSize());
        paging.setSkipCount(pageable.getPageSize() * pageable.getPageNumber());
        searchRoot.setPaging(paging);

        //Sort
        Iterator<Sort.Order> iterator = pageable.getSort().stream().iterator();
        List<AlfrescoSort> sortList = new ArrayList<>();
        while (iterator.hasNext()) {
            Sort.Order sortCriteria = iterator.next();
            AlfrescoSort sort = new AlfrescoSort();
            sort.setAscending(String.valueOf(sortCriteria.isAscending()));
            sort.setField(sortCriteria.getProperty());
            sort.setType(ALFRESCO_SEARCH_CRITERIA_TYPE);
            sortList.add(sort);
        }
        searchRoot.setSort(sortList);


        return alfrescoAuthenticationHelper.getAlfrescoWebClient().post()
                .uri(searchUri.toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(searchRoot))
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocumentRoot.class).block();
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
    public AlfrescoDocument addDocument(String nom, String libelleTypeDocument, AlfrescoTabouType objectType, long objectId, MultipartFile file) throws AppServiceException {

        //Construction de l'uri du document
        String documentUri = DOCUMENT_START_URI + tabouNodeId + CHILDREN_URI + "?autoRename=true";


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
        documentMetadata.setLibelleTypeDocument(libelleTypeDocument);

        //Mise à jour des métadonnées
        updateDocumentMetadata(objectType, objectId, document.getEntry().getId(), documentMetadata, true);

        //Mise à jour du contenu
        updateDocumentContent(objectType, objectId, document.getEntry().getId(), file);

        return getDocumentMetadata(document.getEntry().getId());


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

        properties.setLibelleTypeDocument(documentMetadata.getLibelleTypeDocument());
        AlfrescoMetadata alfrescoMetadata = new AlfrescoMetadata();
        alfrescoMetadata.setProperties(properties);
        alfrescoMetadata.setName(documentMetadata.getNom());


        return alfrescoAuthenticationHelper.getAlfrescoWebClient().put()
                .uri(documentUri.toUriString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(alfrescoMetadata))
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(AlfrescoDocument.class).block();

    }



}
