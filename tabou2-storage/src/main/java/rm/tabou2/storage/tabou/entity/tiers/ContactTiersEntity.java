package rm.tabou2.storage.tabou.entity.tiers;

import java.time.LocalDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

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
    private LocalDateTime dateInactif;
}
