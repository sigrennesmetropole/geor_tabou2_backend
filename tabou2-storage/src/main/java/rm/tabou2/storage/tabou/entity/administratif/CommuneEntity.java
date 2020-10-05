package rm.tabou2.storage.tabou.entity.administratif;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "commune", schema = "limite_admin")
@Table(name = "commune")
public class CommuneEntity {

    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "code_insee")
    private String codeInsee;

}
