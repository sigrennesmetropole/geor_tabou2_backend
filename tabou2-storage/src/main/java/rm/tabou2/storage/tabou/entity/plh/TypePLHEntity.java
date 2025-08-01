package rm.tabou2.storage.tabou.entity.plh;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tabou_type_plh")
public class TypePLHEntity {

	@OrderBy
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_type_plh", nullable = false)
	private long id;

	@Column(name = "libelle", nullable = false)
	private String libelle;

	@Column(name = "date_debut", nullable = false)
	private LocalDateTime dateDebut;

	@Column(name = "date_fin", nullable = false)
	private LocalDateTime dateFin;

	@Column(name = "type_attribut")
	@Enumerated(EnumType.STRING)
	private TypeAttributPLH typeAttributPLH;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_type_plh_parent")
	private Set<TypePLHEntity> fils;

	@Column(name = "selectionnable")
	private boolean selectionnable;

	@Column(name = "order_")
	private int order;

	public void addTypePLHToFils(TypePLHEntity typePLHEntity) {
		if (this.fils == null) {
			this.fils = new HashSet<>();
		}
		this.fils.add(typePLHEntity);
	}
}
