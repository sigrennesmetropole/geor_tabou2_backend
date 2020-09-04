package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_configuration")
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_configuration")
    private long id;

    @Basic
    @Column(name = "comment")
    private String comment;
}
