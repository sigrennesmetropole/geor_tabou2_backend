package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import rm.tabou2.storage.tabou.entity.common.GenericAuditableEntity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tabou_operation")
public class OperationEntity extends GenericAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operation")
    private long id;

    @Basic
    @Column(name = "code")
    private String code;

    @OrderBy
    @Basic
    @Column(name = "nom")
    private String nom;

    @Basic
    @Column(name = "operation")
    private String operation;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "diffusion_restreinte")
    private boolean diffusionRestreinte;

    @Basic
    @Column(name = "est_secteur")
    private Boolean secteur;

    @Basic
    @Column(name = "autorisation_date")
    private Date autorisationDate;

    @Basic
    @Column(name = "operationnel_date")
    private Date operationnelDate;

    @Basic
    @Column(name = "cloture_date")
    private Date clotureDate;

    @Basic
    @Column(name = "surface_totale")
    private Integer surfaceTotale;

    @Basic
    @Column(name = "nb_logement_prevu")
    private Integer nbLogementPrevu;

    @Basic
    @Column(name = "plhlogement_prevu")
    private Integer plhLogementPrevu;

    @Basic
    @Column(name = "QL1")
    private String ql1;

    @Basic
    @Column(name = "QL2")
    private Boolean ql2;

    @Basic
    @Column(name = "QL3")
    private String ql3;

    @Basic
    @Column(name = "nb_entreprise")
    private Integer nbEntreprise;

    @Basic
    @Column(name = "nb_salarie")
    private Integer nbSalarie;

    @Basic
    @Column(name = "plhlogement_livre")
    private Integer plhlogementLivre;

    @Basic
    @Column(name = "num_ads")
    private String numAds;

    @OneToMany(mappedBy = "operation")
    public Set<OperationTiersEntity> operationsTiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_operation")
    public EtapeOperationEntity etapeOperation;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_operation")
    public Set<EvenementOperationEntity> evenements = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nature")
    public NatureEntity nature;

    public void addEvenementOperation(EvenementOperationEntity evenementOperationEntity) {
        this.evenements.add(evenementOperationEntity);
    }

    public Optional<EvenementOperationEntity> lookupEvenementById(long idEvenementOperation) {
        return this.evenements.stream()
                .filter(ep -> ep.getId() == idEvenementOperation)
                .findFirst();
    }

}
