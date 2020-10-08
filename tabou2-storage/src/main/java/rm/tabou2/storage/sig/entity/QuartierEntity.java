package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "quartier", schema = "limite_admin")
public class QuartierEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "nuquart")
    private Short nuQuart;

}
