package rm.tabou2.storage.tabou.entity.common;

import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

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
    private LocalDateTime modifDate;
}
