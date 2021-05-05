package rm.tabou2.service.tabou.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.Tiers;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.item.TiersCriteria;

public interface TiersService {

    Tiers getTiersById(Long tiersId);

    Tiers createTiers(Tiers tiers);

    Tiers updateTiers(Tiers tiers);

    Tiers inactivateTiers(long tiersId) throws AppServiceException;

    Page<Tiers> searchTiers(TiersCriteria tiersCriteria, Pageable pageable);

}
