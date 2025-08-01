package rm.tabou2.storage.tabou.item;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TypeDocumentCriteria {

    private Long id;

    private String libelle;

    private LocalDateTime dateInactif;
}
