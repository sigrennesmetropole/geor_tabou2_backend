package rm.tabou2.storage.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_refresh_token")
public class RefreshToken {

    private long id;
    private String token;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_refresh_token")
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

}
