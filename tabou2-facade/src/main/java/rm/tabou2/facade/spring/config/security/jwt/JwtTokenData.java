package rm.tabou2.facade.spring.config.security.jwt;

public class JwtTokenData {

    // Token JWT
    private String token;

    // Nom unique contenu dans le token
    private String name;

    // Nom d'affichage
    private Object account;

    // Le token est il expiré
    private boolean isExpired;

    // Le token contient t'il des erreurs
    private boolean hasError;


    public Object getAccount() {
        return account;
    }

    public void setAccount(final Object account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(final boolean expired) {
        isExpired = expired;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(final boolean hasError) {
        this.hasError = hasError;
    }
}
