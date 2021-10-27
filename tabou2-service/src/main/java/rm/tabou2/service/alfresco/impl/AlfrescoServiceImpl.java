package rm.tabou2.service.alfresco.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import rm.tabou2.service.alfresco.AlfrescoService;
import rm.tabou2.service.alfresco.dto.AlfrescoDocument;
import rm.tabou2.service.alfresco.dto.AlfrescoDocumentRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoPagination;
import rm.tabou2.service.alfresco.dto.AlfrescoSearchQuery;
import rm.tabou2.service.alfresco.dto.AlfrescoSearchRoot;
import rm.tabou2.service.alfresco.dto.AlfrescoSort;
import rm.tabou2.service.alfresco.dto.AlfrescoTabouType;
import rm.tabou2.service.alfresco.helper.AlfrescoAuthenticationHelper;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.st.generator.model.DocumentContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

@Service
public class AlfrescoServiceImpl implements AlfrescoService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC_AUTHENTIFICATION = "Basic ";
    private static final String DOCUMENT_START_URI = "alfresco/versions/1/nodes/";
    public static final String DOWNLOAD_URI_END = "/content?attachment=true";
    public static final String DELETE_URI_END = "?permanent=false";
    public static final String SEARCH_URI = "search/versions/1/search";

    public static final String SEARCH_PARAM_PROPERTIES = "properties";
    public static final String SEARCH_PARAM_ID = "tabou2:id:";
    public static final String SEARCH_PARAM_LIBELLE_TYPE_DOCUMENT = "tabou2:libelleTypeDocument:";
    public static final String SEARCH_PARAM_OBJET = "tabou2:objet:";

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
    public AlfrescoDocumentRoot searchDocuments(AlfrescoTabouType objectType, long objectId, String nom, String libelle, String typeMime, Pageable pageable) {

        //Construction de l'uri du document
        UriComponentsBuilder searchUri = UriComponentsBuilder
                .fromUriString(SEARCH_URI);


        AlfrescoSearchRoot searchRoot = new AlfrescoSearchRoot();
        AlfrescoSearchQuery searchQuery = new AlfrescoSearchQuery();
        if (!StringUtils.isEmpty(nom)) {
            searchQuery.setQuery(SEARCH_PARAM_ID + objectId);
        }
        if (objectId > 0) {
            searchQuery.setQuery(SEARCH_PARAM_LIBELLE_TYPE_DOCUMENT + libelle);
        }

        //Dans tous les cas, on filtre sur le type d'objet
        searchQuery.setQuery(SEARCH_PARAM_OBJET + objectType);

        //On veut que la requête nous retourne les properties
        ArrayList<String> includes = new ArrayList<>();
        includes.add(SEARCH_PARAM_PROPERTIES);
        searchRoot.setInclude(includes);

        //Paging
        AlfrescoPagination paging = new AlfrescoPagination();
        paging.setMaxItems(paging.getMaxItems());
        paging.setSkipCount(pageable.getPageSize() * pageable.getPageNumber());
        //searchRoot.setPaging(paging); //TODO : à réactiver

        //Sort
        AlfrescoSort sort = new AlfrescoSort();
        sort.setAscending(Boolean.TRUE.toString());
        sort.setField(pageable.getSort().stream().iterator().next().getProperty());
        sort.setType("FIELD"); //TODO : constante


        searchRoot.setQuery(searchQuery);

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
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de récupérer le document id=" + documentId);
        }

        alfrescoAuthenticationHelper.getAlfrescoWebClient().delete()
                .uri(documentUri)
                .header(AUTHORIZATION, BASIC_AUTHENTIFICATION + alfrescoAuthenticationHelper.getAuthenticationTicket())
                .retrieve().bodyToMono(Void.class).block();

    }


}
