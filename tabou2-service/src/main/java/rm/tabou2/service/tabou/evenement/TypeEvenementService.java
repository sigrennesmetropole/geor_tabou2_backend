package rm.tabou2.service.tabou.evenement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.validator.evenement.ValidTypeEvenementCreation;
import rm.tabou2.service.validator.evenement.ValidTypeEvenementUpdate;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

public interface TypeEvenementService {


    TypeEvenement getTypeEvenementById(long typeEvenementId);

    TypeEvenement createTypeEvenement(@ValidTypeEvenementCreation TypeEvenement typeEvenement) throws AppServiceException;

    TypeEvenement updateTypeEvenement(@ValidTypeEvenementUpdate TypeEvenement typeEvenement) throws AppServiceException;

    TypeEvenement inactivateTypeEvenement(Long typeEvenementId) throws AppServiceException;

    Page<TypeEvenement> searchTypeEvenement(TypeEvenementCriteria typeEvenementCriteria, Pageable pageable);
}
