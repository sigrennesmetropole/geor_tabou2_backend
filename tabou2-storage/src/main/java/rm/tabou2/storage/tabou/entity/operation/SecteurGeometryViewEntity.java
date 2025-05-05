package rm.tabou2.storage.tabou.entity.operation;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Geometry;

@Getter
@Setter
@Entity
@Table(name = "v_oa_secteur")
public class SecteurGeometryViewEntity {

    @Id
    @Column(name = "objectid")
    private int id;

    @Basic
    @Column(name = "id_tabou")
    private int idTabou;
    
    @Basic
    @Column(name = "shape", columnDefinition = "Geometry")
    private Geometry shape;

}
