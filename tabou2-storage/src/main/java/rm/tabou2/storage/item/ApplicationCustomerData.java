package rm.tabou2.storage.item;

public class ApplicationCustomerData {

    // Nom de l'application cliente
    private String applicationName;

    // Referrer de l'application cliente
    private String applicationReferrer;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationReferrer() {
        return applicationReferrer;
    }

    public void setApplicationReferrer(String applicationReferrer) {
        this.applicationReferrer = applicationReferrer;
    }
}
