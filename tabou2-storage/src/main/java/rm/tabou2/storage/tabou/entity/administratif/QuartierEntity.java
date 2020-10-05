package rm.tabou2.storage.tabou.entity.administratif;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "quartier", schema = "limite_admin")
@Table(name = "quartier")
public class QuartierEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "nomQuartier")
    private String nom;

    @Basic
    @Column(name = "nuQuartier")
    private String nuQuartier;

}
