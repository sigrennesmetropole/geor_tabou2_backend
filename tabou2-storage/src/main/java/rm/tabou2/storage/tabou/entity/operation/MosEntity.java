package rm.tabou2.storage.tabou.entity.operation;

import lombok.Data;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.ToString;
import org.locationtech.jts.geom.Geometry;

@Data
@Entity
@ToString(exclude = {"geom"})
@Table(name = "tabou_mos") // Correspond au nom de la table SQL
public class MosEntity {

	@Id
	@Column(name = "gid") // Correspond à la clé primaire
	private long gid;

	@Basic
	@Column(name = "code_insee")
	private String codeInsee;

	@Basic
	@Column(name = "nom_commun")
	private String nomCommun;

	@Basic
	@Column(name = "code4_2011")
	private Long code2011;

	@Basic
	@Column(name = "lib4_2011")
	private String libelle2011;

	@Basic
	@Column(name = "remarque11")
	private String remarque2011;

	@Basic
	@Column(name = "code4_2021")
	private Long code2021;

	@Basic
	@Column(name = "lib4_2021")
	private String libelle2021;

	@Basic
	@Column(name = "remarque21")
	private String remarque2021;

	@Basic
	@Column(name = "surface_m2")
	private Double surfaceM2;

	@Basic
	@Column(name = "perimetre")
	private Double perimetre;

	@Basic
	@Column(name = "geom", columnDefinition = "Geometry")
	private Geometry geometry;

}