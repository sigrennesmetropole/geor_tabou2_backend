package rm.tabou2.storage.tabou.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.TypeTiersEntity;

import java.util.Date;

public interface TypeTiersCustomDao {

    Page<TypeTiersEntity> searchTypeTiers(String libelle, Date dateInactif, Pageable pageable);

}
