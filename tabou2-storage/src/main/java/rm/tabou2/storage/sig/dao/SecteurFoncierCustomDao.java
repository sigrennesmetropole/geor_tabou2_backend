package rm.tabou2.storage.sig.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.sig.entity.SecteurFoncierEntity;

public interface SecteurFoncierCustomDao {

    Page<SecteurFoncierEntity> searchSecteursFoncier(String negociateur, Pageable pageable);

}
