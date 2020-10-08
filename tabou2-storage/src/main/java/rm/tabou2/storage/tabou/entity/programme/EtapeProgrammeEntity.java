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

    @OneToMany(mappedBy = "etapeProgramme")
    public Set<ProgrammeEntity> programmes;


}



