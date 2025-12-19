package rm.tabou2.service.tabou.ddc.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rm.tabou2.service.dto.PermisConstruire;
import rm.tabou2.service.mapper.ddc.PermisConstuireMapper;
import rm.tabou2.service.tabou.ddc.PermisConstruireService;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireCustomDao;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermisConstruireServiceImpl implements PermisConstruireService {

    private final PermisConstuireMapper permisConstuireMapper;

    private final PermisConstruireDao permisConstruireDao;

    private final PermisConstruireCustomDao permisConstruireCustomDao;

    private final ProgrammeDao programmeDao;

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
        if (programmeEntity.getNumAds() == null) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return permisConstuireMapper.entitiesToDto(permisConstruireCustomDao.searchPermisConstruire(programmeEntity.getNumAds(), pageable), pageable);
    }

}
