package rm.tabou2.storage.sig.entity;

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

    @OrderBy
    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "code_insee")
    private BigDecimal codeInsee;

    @Basic
    @Column(name = "commune_agglo")
    private Short communeAgglo;


}
