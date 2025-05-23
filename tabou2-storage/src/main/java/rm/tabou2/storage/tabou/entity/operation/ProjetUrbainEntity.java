package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "tabou_projet_urbain")
@NoArgsConstructor
public class ProjetUrbainEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_projet_urbain")
	private long id;

	@Column(name = "title")
	private String title;

	@Column(name = "chapeau")
	private String chapeau;

	@Column(name = "projet")
	private String projet;

	@Column(name = "actualites")
	private String actualites;

	@Column(name = "savoir")
	private String savoir;
}
