package rm.tabou2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.mapper.PermisConstuireMapper;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;
import rm.tabou2.storage.ddc.dao.PermisConstruireDao;

import java.util.Optional;

@Service
public class PermisConstruireServiceImpl {

    /*@Autowired
    PermisConstruireDao permisConstruireDao;*/

    @Autowired
    PermisConstuireMapper permisConstuireMapper;



    public PermisConstruire getPermisConstruireById(long permisConstruireId) {

        Optional<PermisConstruireEntity> permisConstruireOpt = null; //permisConstuireDao.findById(permisConstruireId);

        if (null == permisConstruireOpt || permisConstruireOpt.isEmpty()) {
            //TODO exception
        }

        return null;


    }

}
