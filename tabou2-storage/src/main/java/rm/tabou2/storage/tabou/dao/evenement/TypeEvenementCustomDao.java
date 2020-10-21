package rm.tabou2.storage.tabou.dao.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

public interface TypeEvenementCustomDao {

    Page<TypeEvenementEntity> searchTypeEvenement(TypeEvenementCriteria typeEvenementCriteria, Pageable pageable);


}
