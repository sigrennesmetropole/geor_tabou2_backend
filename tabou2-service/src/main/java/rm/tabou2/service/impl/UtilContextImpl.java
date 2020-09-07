package rm.tabou2.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rm.tabou2.storage.item.ApplicationCustomerData;
import rm.tabou2.storage.item.ConnectedUser;
import rm.tabou2.service.UtilContext;


/**
 * Service utilitaire pour récupérer les infos sur l'utilisateur connecté.
 */
@Component
public class UtilContextImpl implements UtilContext {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(UtilContextImpl.class);

    // Application consommateur de apirmDirectory
    private ApplicationCustomerData ApplicationCustomer;

    /**
     * Retourne l'utilisateur connecté.
     *
     * @return connectedUser
     */
    @Override
    public ConnectedUser getConnectedUser() {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ConnectedUser result = null;
        if (auth == null) {
            LOG.error("auth == null");
        } else {
            final Object principal = auth.getPrincipal();
            if (principal == null) {
                LOG.error("auth.getPrincipal() == null");
            } else {
                if (principal instanceof ConnectedUser) {
                    result = (ConnectedUser) principal;
                } else if ("anonymousAccount".equals(principal)) {
                    LOG.info("Utilisateur anonyme connected");
                } else {
                    LOG.error("Utilisateur autre que anonyme et ConnectedUser");
                }
            }


        }
        return result;
    }

    public ApplicationCustomerData getApplicationCustomer() {
        return ApplicationCustomer;
    }

    public void setApplicationCustomer(ApplicationCustomerData applicationCustomer) {
        ApplicationCustomer = applicationCustomer;
    }
}
