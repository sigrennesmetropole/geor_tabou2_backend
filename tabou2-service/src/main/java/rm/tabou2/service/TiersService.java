package rm.tabou2.service;

import rm.tabou2.service.dto.Tiers;

import java.util.List;

public interface TiersService {
    Tiers addTiers(Tiers tiers);

    List<Tiers> searchTiers(String keyword, Integer start, Boolean onlyActive, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}
