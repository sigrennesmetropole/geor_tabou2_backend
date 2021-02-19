package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.storage.sig.entity.SecteurSpeuEntity;

public interface SecteurSpeuCustomDao {

    @Transactional(transactionManager = "sigTransactionManager", propagation = Propagation.REQUIRED, readOnly = true)
    Page<SecteurSpeuEntity> searchSecteursSpeu(Integer numSecteur, String nomSecteur, Pageable pageable);

}
