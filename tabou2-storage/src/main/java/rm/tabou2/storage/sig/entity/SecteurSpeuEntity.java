package rm.tabou2.storage.sig.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "comite_sect_tab", schema = "limite_admin")
public class SecteurSpeuEntity {

    @Id
    @Column(name = "num_secteur")
    private Integer numSecteur;

    @OrderBy
    @Basic
    @Column(name = "nom_secteur")
    private String nomSecteur;
}
