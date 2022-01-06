package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeActeur;
import rm.tabou2.storage.tabou.item.TypeActeurCriteria;

public interface TypeActeurService {
    Page<TypeActeur> searchTypesActeurs(TypeActeurCriteria criteria, Pageable pageable);
}
