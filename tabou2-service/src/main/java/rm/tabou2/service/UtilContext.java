package rm.tabou2.service;


import rm.tabou2.storage.item.ApplicationCustomerData;
import rm.tabou2.storage.item.ConnectedUser;

/**
 * Service utilitaire pour récupérer les infos sur l'utilisateur connecté.
 */
public interface UtilContext {

    /**
     * Pré requis : un utilisateur est connecté.
     *
     * @return l'utilisateur connecté, null si pas d'utilisateur ou échec de
     * cast.
     */
    ConnectedUser getConnectedUser();

    /**
     * Setter le nom de l'application appelante de Api RM directory
     *
     * @param applicationCustomer
     */
    void setApplicationCustomer(ApplicationCustomerData applicationCustomer);

    /**
     * Get l'application appelante de Api RM Directory
     *
     * @return
     */
    ApplicationCustomerData getApplicationCustomer();
}
