package rm.tabou2.service;

import rm.tabou2.service.dto.Iris;

import java.util.List;

public interface IrisService {

    List<Iris> searchIris(String keyword, Integer start, Integer resultsNumber, String orderBy, Boolean asc) throws Exception;
}
