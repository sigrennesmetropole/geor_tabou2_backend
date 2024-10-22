package rm.tabou2.storage.tabou.entity.agapeo;


import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "tabou_agapeo")
public class AgapeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agapeo")
    private long id;

    @OrderBy
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
    @Column(name = "logements_locatif_aide")
    private Integer logementsLocatifAide;

    @Basic
    @Column(name = "logements_access_aide")
    private Integer logementsAccessAide;

    @Basic
    @Column(name = "logements_locatif_regule_prive")
    private Integer logementsLocatifRegulePrive;

    @Basic
    @Column(name = "logements_locatif_regule_hlm")
    private Integer logementsLocatifReguleHlm;

    @Basic
    @Column(name = "logements_access_maitrise")
    private Integer logementsAccessMaitrise;






}
