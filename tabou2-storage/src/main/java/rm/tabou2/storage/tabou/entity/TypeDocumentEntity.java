package rm.tabou2.storage.tabou.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tabou_type_document")
public class TypeDocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_doc")
    private long id;

    @Basic
    @Column(name = "libelle")
    private String libelle;

    @Basic
    @Column(name = "date_inactif")
    private Date dateInactif;

    @Basic
    @Column(name = "create_user")
    private String createUser;

    @Basic
    @Column(name = "create_date")
    private Date createDate;


}
