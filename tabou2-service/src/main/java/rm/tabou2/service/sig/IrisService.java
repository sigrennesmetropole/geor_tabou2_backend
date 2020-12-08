package rm.tabou2.service.sig;

import rm.tabou2.service.dto.Iris;

import java.util.List;

public interface IrisService {

    List<Iris> searchIris(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc);
}
