package rm.tabou2.service.helper.programme;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import rm.tabou2.storage.sig.dao.ProgrammeRmDao;
import rm.tabou2.storage.sig.entity.ProgrammeRmEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

@Component
@RequiredArgsConstructor
public class ProgrammeEmpriseHelper {

    private final ProgrammeRmDao programmeRmDao;

    @Named("getIdEmpriseProgramme")
    public Integer getIdEmpriseProgramme(ProgrammeEntity programme){
        ProgrammeRmEntity emprise = programmeRmDao.getByIdTabou(Math.toIntExact(programme.getId()));
        if(emprise != null){
            return emprise.getId();
        }
        return null;
    }

}
