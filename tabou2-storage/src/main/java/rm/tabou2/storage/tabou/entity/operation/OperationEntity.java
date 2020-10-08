package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_operation")
public class OperationEntity {

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
    @Column(name = "diffusion_retreinte")
    private Boolean diffusionRetreinte;

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
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @Column(name = "create_date")
    private Date createDate;

    @Basic
    @Column(name = "modif_user")
    private String modifUser;

    @Basic
    @Column(name = "modif_date")
    private Date modifDate;

    @Basic
    @Column(name = "num_ads")
    private String numAds;

    @OneToMany(mappedBy = "operation")
    public Set<OperationTiersEntity> operationsTiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etape_operation")
    public EtapeOperationEntity etapeOperation;

    @OneToMany(mappedBy = "operation")
    public Set<EvenementOperationEntity> evenements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nature")
    public NatureEntity nature;





}
