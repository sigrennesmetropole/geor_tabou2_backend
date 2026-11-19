package rm.tabou2.storage.tabou.entity.operation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Vue légère en lecture seule (US-323) : une ligne par opération et commune majoritaire d'une de ses emprises.
 * Utilisée par la règle de visibilité à la place de v_oa_operation, que PostgreSQL ne peut pas filtrer par opération.
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "v_oa_operation_commune")
@IdClass(OperationCommuneViewEntity.Key.class)
public class OperationCommuneViewEntity {

    @Id
    @Column(name = "id_tabou")
    private Integer idTabou;

    @Id
    @Column(name = "code_insee")
    private BigDecimal codeInsee;

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Key implements Serializable {

        private Integer idTabou;

        private BigDecimal codeInsee;
    }
}
