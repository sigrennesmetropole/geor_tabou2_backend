package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "negociateurfoncier_secteur", schema = "urba_foncier")
public class SecteurFoncierEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @Basic
    @Column(name = "negociateur")
    private String negociateur;

}
