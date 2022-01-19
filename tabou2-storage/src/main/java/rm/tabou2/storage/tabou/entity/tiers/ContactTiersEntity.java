package rm.tabou2.storage.tabou.entity.tiers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_contact_tiers")
@NoArgsConstructor
@AllArgsConstructor
public class ContactTiersEntity extends GenericAuditableEntity {

    @Id
    @OrderBy
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact_tiers", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fonction_contact", nullable = false)
    public FonctionContactsEntity fonctionContact;

    @Basic
    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Basic
    @Column(name = "prenom", length = 50)
    private String prenom;

    @Basic
    @Column(name = "service", length = 50)
    private String service;

    @Basic
    @Column(name = "adresse", length = 100)
    private String adresse;

    @Basic
    @Column(name = "adresse_cp", length = 20)
    private String adresseCp;

    @Basic
    @Column(name = "adresse_ville", length = 50)
    private String adresseVille;

    @Basic
    @Column(name = "telecopie", length = 50)
    private String telecopie;

    @Basic
    @Column(name = "telephone", length = 50)
    private String telephone;

    @Basic
    @Column(name = "email", length = 100)
    private String email;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;
}
