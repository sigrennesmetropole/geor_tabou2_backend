package rm.tabou2.storage.sig.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "oa_programme", schema = "urba_foncier")
public class ProgrammeRmEntity {

    @Id
    @Column(name = "objectid")
    private Integer id;

    @OrderBy
    @Column(name = "programme")
    private String programme;

    @Column(name = "id_tabou")
    private Integer idTabou;

}
