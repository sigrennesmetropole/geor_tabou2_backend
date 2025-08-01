package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import rm.tabou2.storage.tabou.entity.common.AbstractOrderEntity;

@Data
@Entity
@Table(name = "tabou_outil_amenagement")
public class OutilAmenagementEntity extends AbstractOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "code")
    private String code;
}
