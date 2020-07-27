package rm.tabou2.service.items;

public class Tokens {

    private String jwtToken;
    private String refreshToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(final String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
