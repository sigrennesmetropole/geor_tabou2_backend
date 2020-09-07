package rm.tabou2.storage.tabou.entity;

import lombok.Data;
import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Authorité de l'utilisateur connecté.
 */
@Data
@Entity
@Immutable
@Table(name = "t_authority")
public class Authority implements GrantedAuthority {

    private long id;
    private String authority;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_authority")
    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "code")
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(final String authority) {
        this.authority = authority;
    }


}