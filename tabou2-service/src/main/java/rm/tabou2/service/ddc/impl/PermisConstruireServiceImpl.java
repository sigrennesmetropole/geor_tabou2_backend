package rm.tabou2.service.ddc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.ddc.PermisConstruireService;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.mapper.ddc.PermisConstuireMapper;
import rm.tabou2.storage.ddc.dao.PermisConstruireCustomDao;
import rm.tabou2.storage.ddc.dao.PermisConstruireDao;
import rm.tabou2.storage.ddc.entity.PermisConstruireEntity;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PermisConstruireServiceImpl implements PermisConstruireService {

    @Autowired
    PermisConstuireMapper permisConstuireMapper;

    @Autowired
    PermisConstruireDao permisConstruireDao;

    @Autowired
    PermisConstruireCustomDao permisConstruireCustomDao;

    @Autowired
    ProgrammeDao programmeDao;

    public PermisConstruire getPermisConstruireById(long permisConstruireId) {

        Optional<PermisConstruireEntity> permisConstruireOpt = permisConstruireDao.findById(permisConstruireId);

        if (permisConstruireOpt.isEmpty()) {
            throw new NoSuchElementException("le permis de construire id = " + permisConstruireId + " n'existe pas");
        }

        return permisConstuireMapper.entityToDto(permisConstruireOpt.get());

    }

    @Override
    public Page<PermisConstruire> getPermisConstruiresByProgrammeId(long programmeId, Pageable pageable) {

        ProgrammeEntity programmeEntity = programmeDao.findOneById(programmeId);
        if (programmeEntity == null) {
            throw new IllegalArgumentException("L'identifiant du programme est invalide: aucun programme trouvé pour l'id = " + programmeId);
        }

        return permisConstuireMapper.entitiesToDto(permisConstruireCustomDao.searchPermisConstruire(programmeEntity.getNumAds(), pageable), pageable);
    }

}
