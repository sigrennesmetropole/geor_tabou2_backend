package rm.tabou2.storage.ddc.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "pc_ddc")
public class PermisConstruireEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "num_pc")
    private String numPc;

    @Basic
    @Column(name = "depot_dossier")
    private Date depotDossier;

    @Basic
    @Column(name = "completude_dossier")
    private Date completudeDossier;

    @Basic
    @Column(name = "decision_dossier")
    private String decisionDossier;

    @Basic
    @Column(name = "surf_bureaux")
    private Double surfBureaux = null;

    @Basic
    @Column(name = "surf_commerces")
    private Double surfCommerces = null;

    @Basic
    @Column(name = "surf_industries")
    private Double surfIndustries = null;

    @Basic
    @Column(name = "surf_equip_pub")
    private Double surfEquipPub = null;

    @Basic
    @Column(name = "surf_autre")
    private Double surfAutre = null;


}
