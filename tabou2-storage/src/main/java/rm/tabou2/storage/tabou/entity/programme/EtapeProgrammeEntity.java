package rm.tabou2.storage.tabou.entity.programme;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "tabou_etape_programme")
public class EtapeProgrammeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etape_programme")
    private long id;

    @OrderBy
    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "mode")
    private String mode;

    @Basic
    @Column(name = "type")
    private String type;

    @OneToMany
    @JoinTable(
            name = "tabou_etape_programme_workflow",
            joinColumns = @JoinColumn(name = "id_etape_programme", referencedColumnName = "id_etape_programme"),
            inverseJoinColumns = @JoinColumn(name = "id_etape_programme_next", referencedColumnName = "id_etape_programme")
    )
    private Set<EtapeProgrammeEntity> nextEtapes;

}



