package rm.tabou2.service;

import rm.tabou2.service.dto.Nature;
import rm.tabou2.storage.tabou.entity.NatureEntity;

import java.util.List;

public interface NatureService {


    Nature addNature(Nature nature);

    void inactivateNature(long natureId);

    List<Nature> getAllNatures(Boolean onlyActive) throws Exception;

}
