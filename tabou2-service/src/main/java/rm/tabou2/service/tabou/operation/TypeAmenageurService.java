package rm.tabou2.service.tabou.operation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.item.TypeAmenageurCriteria;
import rm.tabou2.service.dto.TypeAmenageur;

public interface TypeAmenageurService {

    Page<TypeAmenageur> searchTypesAmenageurs(TypeAmenageurCriteria criteria, Pageable pageable);
}
