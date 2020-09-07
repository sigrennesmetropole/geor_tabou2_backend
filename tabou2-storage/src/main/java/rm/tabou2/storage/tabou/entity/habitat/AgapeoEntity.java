package rm.tabou2.storage.tabou.entity.habitat;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "agapeo")
public class AgapeoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "logements_access_sociale")
    private int logementsAccessSociale;

    @Basic
    @Column(name = "logements_maitrise")
    private int logementsMaitrise;

    @Basic
    @Column(name = "logements_locat_regul")
    private int logementsLocatRegul;

    @Basic
    @Column(name = "logements_pls")
    private int logementsPls;

    @Basic
    @Column(name = "logements_locat_social")
    private int logementsLocatSocial;

    @Basic
    @Column(name = "annee_agre_etat")
    private String anneeAgrementEtat;

    @Basic
    @Column(name = "plh_logements_prevys")
    private Long plhLogementsPrevus = null;

    @Basic
    @Column(name = "plh_logements_livres")
    private Long plhLogementsLivres = null;

    @Basic
    @Column(name = "num_pc")
    private String numPc = null;


}
