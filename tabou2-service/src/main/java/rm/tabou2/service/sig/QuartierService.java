package rm.tabou2.service.sig;

import rm.tabou2.service.dto.Quartier;

import java.util.List;

public interface QuartierService {
    List<Quartier> searchQuartiers(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);
}