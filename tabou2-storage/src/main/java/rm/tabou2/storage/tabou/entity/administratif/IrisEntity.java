package rm.tabou2.storage.tabou.entity.administratif;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "iris", schema = "limite_admin")
public class IrisEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "nom")
    private String nom;

}
