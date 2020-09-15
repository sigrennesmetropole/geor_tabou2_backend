package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "operation")
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "code")
    private String code;

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
    @Column(name = "secteur")
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
    @Column(name = "logement_prevu")
    private Integer logementPrevu;

    @Basic
    @Column(name = "plhlogement_prevu")
    private Integer plhLogementPrevu;

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

    @OneToMany(mappedBy = "operation")
    public Set<OperationTiersEntity> operationsTiers;

    @OneToMany(mappedBy = "operation")
    public Set<EtapeOperationEntity> etapes;

    @OneToMany(mappedBy = "operation")
    public Set<EvenementOperationEntity> evenements;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nature")
    public NatureEntity nature;

}
