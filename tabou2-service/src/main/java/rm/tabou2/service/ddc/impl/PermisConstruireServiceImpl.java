package rm.tabou2.service.ddc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.mapper.ddc.PermisConstuireMapper;
import rm.tabou2.storage.ddc.dao.PermisConstruireDao;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PermisConstruireServiceImpl {

    @Autowired
    PermisConstuireMapper permisConstuireMapper;

    @Autowired
    PermisConstruireDao permisConstruireDao;

    public PermisConstruire getPermisConstruireById(long permisConstruireId) {

        Optional<PermisConstruireEntity> permisConstruireOpt = permisConstruireDao.findById(permisConstruireId);

        if (permisConstruireOpt.isEmpty()) {
            throw new NoSuchElementException("le permis de construire id = " + permisConstruireId + " n'existe pas");
        }

        return null;


    }

}
