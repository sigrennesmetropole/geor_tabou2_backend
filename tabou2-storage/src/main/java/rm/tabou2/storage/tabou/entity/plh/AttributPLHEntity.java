package rm.tabou2.storage.tabou.entity.plh;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tabou_attribut_plh")
public class AttributPLHEntity {

	@OrderBy
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_attribut_plh", nullable = false)
	private long id;

	@Column(name = "value_")
	private String value;

	@ManyToOne
	@JoinColumn(name = "id_type_plh")
	private TypePLHEntity type;
}
