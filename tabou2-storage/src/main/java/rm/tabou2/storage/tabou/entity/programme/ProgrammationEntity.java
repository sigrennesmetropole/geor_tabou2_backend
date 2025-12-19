package rm.tabou2.storage.tabou.entity.programme;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tabou_programmation")
@Data
public class ProgrammationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_programmation")
	private long id;

	@Basic
	@Column(name = "surface_bureaux")
	private Double surfaceBureaux;

	@Basic
	@Column(name = "surface_commerces")
	private Double surfaceCommerces;

	@Basic
	@Column(name = "surface_industrie")
	private Double surfaceIndustrie;

	@Basic
	@Column(name = "surface_equipements")
	private Double surfaceEquipements;

	@Basic
	@Column(name = "surface_autres")
	private Double surfaceAutres;


}
