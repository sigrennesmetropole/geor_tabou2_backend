package rm.tabou2.storage.tabou.dao.financement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import rm.tabou2.storage.tabou.entity.financement.TypeFinancementEntity;

public interface TypeFinancementDao  extends CrudRepository<TypeFinancementEntity, Long>, JpaRepository<TypeFinancementEntity, Long> {

}
