package rm.tabou2.storage.tabou.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class GenericAuditableEntity extends GenericCreateAuditableEntity {

    @Basic
    @LastModifiedBy
    @Column(name = "modif_user")
    private String modifUser;

    @Basic
    @LastModifiedDate
    @Column(name = "modif_date")
    private Date modifDate;
}
