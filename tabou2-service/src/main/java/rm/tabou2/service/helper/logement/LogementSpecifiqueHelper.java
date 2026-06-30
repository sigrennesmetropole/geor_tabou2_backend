package rm.tabou2.service.helper.logement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.LogementSpecifique;
import rm.tabou2.service.dto.LogementsSpecifiques;
import rm.tabou2.service.dto.TypeAccessionLogement;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.storage.tabou.dao.logement.LogementsSpecifiquesDao;
import rm.tabou2.storage.tabou.dao.logement.TypeAccessionLogementDao;
import rm.tabou2.storage.tabou.dao.logement.TypeLogementDao;
import rm.tabou2.storage.tabou.entity.logement.*;
import rm.tabou2.storage.tabou.item.TypeAccessionLogementCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Helper factorisé pour la gestion CRUD des LogementsSpecifiques et de leurs enfants LogementSpecifique.
 * Utilisé par OperationUpdateHelper et ProgrammeServiceImpl.
 */
@Component
@RequiredArgsConstructor
public class LogementSpecifiqueHelper {

    private final LogementsSpecifiquesDao logementsSpecifiquesDao;

    private final TypeAccessionLogementDao typeAccessionLogementDao;

    private final TypeLogementDao typeLogementDao;

    /**
     * Met à jour la liste des LogementsSpecifiques d'un parent (opération ou programmation habitat).
     * Gère la suppression, la mise à jour et l'ajout des éléments et de leurs enfants.
     *
     * @param dtos       liste des DTOs source
     * @param actualList liste actuelle des entités (collection du parent, modifiée en place)
     */
    public void updateLogementsSpecifiques(List<LogementsSpecifiques> dtos, List<LogementsSpecifiquesEntity> actualList) {
        if (dtos == null) {
            return;
        }

        // Si la liste est vide, supprimer tous les logements spécifiques existants
        if (dtos.isEmpty()) {
            List<LogementsSpecifiquesEntity> toRemove = new ArrayList<>(actualList);
            actualList.clear();
            toRemove.forEach(logementsSpecifiquesDao::delete);
            return;
        }

        List<LogementsSpecifiquesEntity> snapshot = new ArrayList<>(actualList);

        // Suppression des logements spécifiques qui ne sont plus dans la liste
        List<LogementsSpecifiquesEntity> aSupprimer = new ArrayList<>();
        for (LogementsSpecifiquesEntity logementEntity : snapshot) {
            boolean stillPresent = dtos.stream()
                    .anyMatch(l -> l.getId() != null && logementEntity.getId() == l.getId());
            if (!stillPresent) {
                actualList.remove(logementEntity);
                aSupprimer.add(logementEntity);
            }
        }
        if (!aSupprimer.isEmpty()) {
            logementsSpecifiquesDao.deleteAll(aSupprimer);
        }

        // Ajout ou mise à jour des logements spécifiques
        for (LogementsSpecifiques logement : dtos) {
            Optional<LogementsSpecifiquesEntity> existing = snapshot.stream()
                    .filter(entity -> Long.valueOf(entity.getId()).equals(logement.getId()))
                    .findFirst();
            if (existing.isPresent()) {
                updateExistingLogementSpecifique(existing.get(), logement);
            } else {
                addNewLogementSpecifique(logement, actualList);
            }
        }
    }

    private void updateExistingLogementSpecifique(LogementsSpecifiquesEntity toUpdate, LogementsSpecifiques logement) {
        toUpdate.setValeur(logement.getValeur());
        setTypeAccessionIfPresent(toUpdate, logement);
        updateLogementSpecifiqueChildren(logement.getLogements(), toUpdate);
        logementsSpecifiquesDao.save(toUpdate);
    }

    private void addNewLogementSpecifique(LogementsSpecifiques logement, List<LogementsSpecifiquesEntity> actualList) {
        LogementsSpecifiquesEntity toAdd = new LogementsSpecifiquesEntity();
        toAdd.setValeur(logement.getValeur());
        setTypeAccessionIfPresent(toAdd, logement);

        // Ajout des enfants LogementSpecifique
        if (logement.getLogements() != null) {
            for (LogementSpecifique childDto : logement.getLogements()) {
                addNewLogementSpecifiqueChild(childDto, toAdd);
            }
        }

        toAdd = logementsSpecifiquesDao.save(toAdd);
        actualList.add(toAdd);
    }

    /**
     * Mise à jour des enfants LogementSpecifique d'un LogementsSpecifiquesEntity.
     */
    private void updateLogementSpecifiqueChildren(List<LogementSpecifique> dtoLogements, LogementsSpecifiquesEntity parent) {
        List<LogementSpecifique> children = dtoLogements != null ? dtoLogements : List.of();
        List<LogementSpecifiqueEntity> actualLogements = new ArrayList<>(parent.getLogements());

        // Suppression des enfants qui ne sont plus dans la liste
        for (LogementSpecifiqueEntity childEntity : actualLogements) {
            boolean stillPresent = children.stream()
                    .anyMatch(l -> l.getId() != null && childEntity.getId() == l.getId());
            if (!stillPresent) {
                parent.getLogements().remove(childEntity);
            }
        }

        // Ajout ou mise à jour des enfants
        for (LogementSpecifique childDto : children) {
            Optional<LogementSpecifiqueEntity> existingChild = actualLogements.stream()
                    .filter(entity -> Long.valueOf(entity.getId()).equals(childDto.getId()))
                    .findFirst();
            if (existingChild.isPresent()) {
                updateExistingLogementSpecifiqueChild(existingChild.get(), childDto);
            } else {
                addNewLogementSpecifiqueChild(childDto, parent);
            }
        }
    }

    private void updateExistingLogementSpecifiqueChild(LogementSpecifiqueEntity toUpdate, LogementSpecifique childDto) {
        toUpdate.setValeurPrevue(childDto.getValeurPrevue());
        toUpdate.setValeurRealisee(childDto.getValeurRealisee());
        setTypeLogementIfPresent(toUpdate, childDto);
    }

    private void addNewLogementSpecifiqueChild(LogementSpecifique childDto, LogementsSpecifiquesEntity parent) {
        LogementSpecifiqueEntity child = new LogementSpecifiqueEntity();
        child.setValeurPrevue(childDto.getValeurPrevue());
        child.setValeurRealisee(childDto.getValeurRealisee());
        setTypeLogementIfPresent(child, childDto);
        parent.getLogements().add(child);
    }

    private void setTypeLogementIfPresent(LogementSpecifiqueEntity entity, LogementSpecifique logement) {
        TypeLogement typeLogement = logement.getTypeLogement();
        if (typeLogement != null && typeLogement.getId() != null) {
            entity.setTypeLogement(typeLogementDao.getReferenceById(Objects.requireNonNull(typeLogement.getId())));
        }
    }

    private void setTypeAccessionIfPresent(LogementsSpecifiquesEntity entity, LogementsSpecifiques logement) {
        TypeAccessionLogement typeAccession = logement.getTypeAccessionLogement();
        if (typeAccession != null && typeAccession.getId() != null) {
            entity.setTypeAccessionLogement(typeAccessionLogementDao.getReferenceById(Objects.requireNonNull(typeAccession.getId())));
        }
    }

    /**
     * Initialise les LogementsSpecifiques d'un parent (opération ou programmation habitat)
     * avec tous les TypeAccessionLogement et TypeLogement actuellement actifs en base.
     * <p>
     * Pour chaque TypeAccessionLogement actif correspondant à la portée donnée,
     * crée un LogementsSpecifiquesEntity contenant un LogementSpecifiqueEntity
     * pour chaque TypeLogement actif (avec valeurs prévues et réalisées à null).
     *
     * @param portee     portée des types d'accession à utiliser (PROGRAMME ou OPERATION)
     * @param targetList liste cible dans laquelle ajouter les entités créées
     */
    public void initializeLogementsSpecifiques(PorteeAccessionLogement portee, List<LogementsSpecifiquesEntity> targetList) {
        TypeAccessionLogementCriteria criteria = TypeAccessionLogementCriteria.builder()
                .actifUniquement(true)
                .portee(portee)
                .build();

        List<TypeAccessionLogementEntity> typesAccession = typeAccessionLogementDao
                .searchTypeAccessionLogements(criteria, Pageable.unpaged())
                .getContent();
        List<TypeLogementEntity> typesLogement = typeLogementDao
                .searchTypeLogements(null, true, Pageable.unpaged())
                .getContent();

        if (typesAccession.isEmpty()) {
            return;
        }

        List<LogementsSpecifiquesEntity> aCreer = new ArrayList<>();
        for (TypeAccessionLogementEntity typeAccession : typesAccession) {
            LogementsSpecifiquesEntity logementsSpec = new LogementsSpecifiquesEntity();
            logementsSpec.setTypeAccessionLogement(typeAccession);

            for (TypeLogementEntity typeLogement : typesLogement) {
                LogementSpecifiqueEntity logementSpec = new LogementSpecifiqueEntity();
                logementSpec.setTypeLogement(typeLogement);
                logementsSpec.getLogements().add(logementSpec);
            }

            aCreer.add(logementsSpec);
        }
        targetList.addAll(logementsSpecifiquesDao.saveAll(aCreer));
    }
}

