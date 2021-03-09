package rm.tabou2.storage.tabou.dao.programme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.storage.tabou.entity.programme.EtapeProgrammeEntity;
import rm.tabou2.storage.tabou.item.EtapeCriteria;

public interface EtapeProgrammeCustomDao {

    Page<EtapeProgrammeEntity> searchEtapeProgrammes(EtapeCriteria etapeCriteria, Pageable pageable);

}
