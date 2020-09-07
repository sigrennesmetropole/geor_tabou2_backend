package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "etape_programme")
public class EtapeProgrammeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_tiers")
    public TypeTiersEntity typeTiers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_programme")
    public ProgrammeEntity programme;


}



