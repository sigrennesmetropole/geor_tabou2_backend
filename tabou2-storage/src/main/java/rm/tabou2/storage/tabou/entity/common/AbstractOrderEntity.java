package rm.tabou2.storage.tabou.entity.common;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractOrderEntity {

    @Basic
    @Column(name = "order_")
    private Integer order;

}
