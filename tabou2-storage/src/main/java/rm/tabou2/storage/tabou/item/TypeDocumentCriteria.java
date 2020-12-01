package rm.tabou2.storage.tabou.item;

import lombok.Data;

import java.util.Date;

@Data
public class TypeDocumentCriteria {

    private Long id;

    private String libelle;

    private Date dateInactif;
}
