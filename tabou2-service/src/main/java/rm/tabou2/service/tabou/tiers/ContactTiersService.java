package rm.tabou2.service.tabou.tiers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.storage.tabou.item.ContactTiersCriteria;

import rm.tabou2.service.dto.ContactTiers;

public interface ContactTiersService {

    /**
     * Récupération d'un contact de tiers à partir de son id
     * @param tiersId id du tiers au quel est rattaché le contact
     * @param contactTiersId id du contact de tiers à récupérer
     * @return le ContactTiers correspondant
     */
    ContactTiers getContactTiersById(Long tiersId, Long contactTiersId);

    /**
     * Création d'un contact de tiers
     * @param tiersId id du tiers au quel est rattaché le contact.
     * @param contactTiers contact à ajoter
     * @return le ContactTiers créé
     */
    ContactTiers createContactTiers(Long tiersId, ContactTiers contactTiers);

    /**
     * Mise à jour d'un contact de tiers
     * @param tiersId id du tiers auquel est rattaché le contact
     * @param contactTiers contact à éditer
     * @return le ContactTiers modifié
     */
    ContactTiers updateContactTiers(Long tiersId, ContactTiers contactTiers);

    /**
     * Rends inactif un contact de tiers
     * @param tiersId id du tiers auquel est rattaché le contact
     * @param contactTiersId contact à rendre inactif
     * @return le ContactTiers rendu inactif
     * @throws AppServiceException
     */
    ContactTiers inactivateContactTiers(Long tiersId, long contactTiersId) throws AppServiceException;

    /**
     * Recherche les contacts tiers d'un tiers
     * @param criteria les critères de recherche des contacts de tiers
     * @param pageable Pagebale indiquant le nombre d'éléments à retourner et à partir duquel
     * @return Les ContactTiers correspondant à la recherche
     */
    Page<ContactTiers> searchContactTiers(ContactTiersCriteria criteria, Pageable pageable);
}
