package rm.tabou2.service.alfresco.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rm.tabou2.service.alfresco.dto.AlfrescoTicket;
import rm.tabou2.service.alfresco.dto.AlfrescoUser;

import java.util.Base64;

@Component
public class AlfrescoAuthenticationHelper {

    @Value("${alfresco.authenticate.user}")
    private String alfrescoUsername;

    @Value("${alfresco.authenticate.password}")
    private String alfrescoPassword;

    @Value("${alfresco.base.url}")
    private String alfrescoBaseUrl;

    public static final String ALFRESCO_TICKET_URL = "authentication/versions/1/tickets";

    private WebClient alfrescoWebClient;

    public String getAuthenticationTicket() {

        //Création de l'utilisateur Alfresco
        AlfrescoUser alfrescoUser = new AlfrescoUser(alfrescoUsername, alfrescoPassword);

        //Génération du ticket
        AlfrescoTicket ticket = getAlfrescoWebClient().post().uri(ALFRESCO_TICKET_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(alfrescoUser))
                .retrieve().bodyToMono(AlfrescoTicket.class).block();

        if(ticket == null){
            return null;
        }
        //Encodage du ticket en base 64
        return Base64.getEncoder().encodeToString(ticket.getEntry().getId().getBytes());

    }

    public WebClient getAlfrescoWebClient() {
        if(alfrescoWebClient == null){
            alfrescoWebClient = WebClient.builder()
                    .baseUrl(alfrescoBaseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();
        }
        return alfrescoWebClient;
    }


}
