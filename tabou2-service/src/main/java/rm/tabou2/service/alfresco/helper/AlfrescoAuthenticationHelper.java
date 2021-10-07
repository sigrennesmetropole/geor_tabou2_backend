package rm.tabou2.service.alfresco.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import rm.tabou2.service.alfresco.dto.AlfrescoTicket;
import rm.tabou2.service.alfresco.dto.AlfrescoUser;

import javax.annotation.PostConstruct;
import java.util.Base64;

@Component
public class AlfrescoAuthenticationHelper {

    @Value("${alfresco.authenticate.user}")
    private String alfrescoUsername;

    @Value("${alfresco.authenticate.password}")
    private String alfrescoPassword;

    @Value("${alfresco.base.url}")
    private String alfrescoBaseUrl;

    private String authenticationTicket;

    public final static String ALFRESCO_TICKET_URL = "authentication/versions/1/tickets";

    @Autowired
    private WebClient alfrescoWebClient;

    @PostConstruct
    public void initAuthenticationTicket() {
        authenticationTicket = generateAuthenticationTicket();
    }

    public void resetAuhenticationTicket() {
        authenticationTicket = generateAuthenticationTicket();
    }

    public String getAuthenticationTicket() {
        return authenticationTicket;
    }

    private String generateAuthenticationTicket() {

        //Création de l'utilisateur Alfresco
        AlfrescoUser alfrescoUser = new AlfrescoUser(alfrescoUsername, alfrescoPassword);

        //Génération du ticket
        AlfrescoTicket ticket = alfrescoWebClient.post().uri(ALFRESCO_TICKET_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(alfrescoUser))
                .retrieve().bodyToMono(AlfrescoTicket.class).block();

        //Encodage du ticket en base 64
        return Base64.getEncoder().encodeToString(ticket.getEntry().getId().getBytes());

    }

    @Bean(name = "alfrescoWebClient")
    public WebClient getAlfrescoWebClient() {
        return WebClient.builder()
                .baseUrl(alfrescoBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


}
