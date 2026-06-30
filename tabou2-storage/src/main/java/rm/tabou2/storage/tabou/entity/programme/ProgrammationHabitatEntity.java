package rm.tabou2.storage.tabou.entity.programme;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import rm.tabou2.storage.tabou.entity.logement.LogementsSpecifiquesEntity;

@Getter
@Setter
@Entity
@Table(name = "tabou_programmation_habitat")
public class ProgrammationHabitatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programmation_habitat")
    private long id;

    @Basic
    @Column(name = "nb_logements")
    private Integer nbLogements;

    @Basic
    @Column(name = "nb_logements_hfv")
    private Integer nbLogementsHFV;

    @Basic
    @Column(name = "surface_shab")
    private Double surfaceSHAB;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "tabou_prog_habitat_logements_sp",
            joinColumns = @JoinColumn(name = "id_programmation_habitat"),
            inverseJoinColumns = @JoinColumn(name = "id_logements_specifiques")
    )
    private List<LogementsSpecifiquesEntity> logementsSpecifiques = new ArrayList<>();
}

