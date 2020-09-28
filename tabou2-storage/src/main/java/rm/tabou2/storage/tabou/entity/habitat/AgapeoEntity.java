package rm.tabou2.storage.tabou.entity.habitat;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tabou_agapeo")
public class AgapeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agapeo")
    private long id;

    @Basic
    @Column(name = "annee_prog")
    private Integer anneeProg;

    @Basic
    @Column(name = "etat")
    private String etat;

    @Basic
    @Column(name = "maitre_ouvrage")
    private String maitreOuvrage;

    @Basic
    @Column(name = "commune")
    private String commune;

    @Basic
    @Column(name = "nom_operation")
    private String nomOperation;

    @Basic
    @Column(name = "num_dossier")
    private Integer numDossier;

    @Basic
    @Column(name = "num_ads")
    private String numAds = null;

    @Basic
    @Column(name = "convention_application_plh")
    private String conventionApplicationPlh = null;

    @Basic
    @Column(name = "logements_locat_aide")
    private int logementsLocatAide;

    @Basic
    @Column(name = "logements_access_aide")
    private int logementsAccessAide;

    @Basic
    @Column(name = "logements_locat_regul_prive")
    private int logementsLocatRegulPrive;

    @Basic
    @Column(name = "logements_locat_regul_hlm")
    private int logementsLocatRegulHlm;



    @Basic
    @Column(name = "logements_access_maitrise")
    private int logementsAccessMaitrise;






}
