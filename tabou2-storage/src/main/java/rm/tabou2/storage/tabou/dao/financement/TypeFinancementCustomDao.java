package rm.tabou2.storage.tabou.dao.financement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;
import rm.tabou2.storage.tabou.item.TypeFinancementCriteria;

public interface TypeFinancementCustomDao {

    Page<TypeFinancementEntity> searchTypeFinancement(TypeFinancementCriteria typeFinancementCriteria, Pageable pageable);

}
