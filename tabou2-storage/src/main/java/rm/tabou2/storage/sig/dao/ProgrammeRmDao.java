package rm.tabou2.storage.sig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rm.tabou2.storage.common.CustomCrudRepository;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;

public interface ProgrammeRmDao extends CustomCrudRepository<ProgrammeRmEntity, Integer>, JpaRepository<ProgrammeRmEntity, Integer> {

    ProgrammeRmEntity getByIdTabou(Integer idTabou);

}
