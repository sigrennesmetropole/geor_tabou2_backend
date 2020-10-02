package rm.tabou2.storage.tabou.entity.administratif;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "commune_emprise", schema = "limite_admin")
public class CommuneEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "code_insee")
    private BigDecimal codeInsee;

    @Basic
    @Column(name = "code_postal")
    private String codePostal;

}
