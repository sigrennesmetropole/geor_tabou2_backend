package rm.tabou2.storage.tabou.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class GenericCreateAuditableEntity {

    @Basic
    @CreatedBy
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;
}
