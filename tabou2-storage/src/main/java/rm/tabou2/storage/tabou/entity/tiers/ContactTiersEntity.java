package rm.tabou2.storage.tabou.entity.tiers;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_contact_tiers")
public class ContactTiersEntity extends GenericAuditableEntity {

    @Id
    @OrderBy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact_tiers", nullable = false)
    private long id;

    @Basic()
    @Column(name = "id_tiers", nullable = false)
    private long idTiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fonction_contact", nullable = false)
    public FonctionContactsEntity fonctionContact;

    @Basic
    @Column(name = "nom", nullable = false)
    private String nom;

    @Basic
    @Column(name = "prenom")
    private String prenom;

    @Basic
    @Column(name = "service")
    private String service;

    @Basic
    @Column(name = "adresse")
    private String adresse;

    @Basic
    @Column(name = "adresse_cp")
    private String adresseCp;

    @Basic
    @Column(name = "adresse_ville")
    private String adresseVille;

    @Basic
    @Column(name = "telecopie")
    private String telecopie;

    @Basic
    @Column(name = "telephone")
    private String telephone;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;
}
