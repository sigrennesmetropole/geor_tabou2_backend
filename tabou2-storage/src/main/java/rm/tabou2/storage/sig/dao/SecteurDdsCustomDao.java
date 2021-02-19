package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.sig.entity.SecteurDdsEntity;

public interface SecteurDdsCustomDao {
    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    Page<SecteurDdsEntity> searchSecteursDds(String secteur, Pageable pageable);
}
