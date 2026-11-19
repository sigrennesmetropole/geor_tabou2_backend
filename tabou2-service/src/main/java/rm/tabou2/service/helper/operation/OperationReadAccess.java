package rm.tabou2.service.helper.operation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.sig.CommuneResolver;
import rm.tabou2.storage.tabou.dao.operation.OperationCustomDao;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationVisibilityFilter;

/**
 * Point unique de la règle de visibilité des opérations (US-323), partagé par les services REST
 * et, en phase 5, par le proxy cartographique.
 * <ul>
 * <li>GLOBAL : Référents et Administrateurs, tout est visible ;</li>
 * <li>COMMUNE : organisation reconnue comme commune de l'agglomération ;</li>
 * <li>AUCUNE : organisation inconnue, opérations publiques uniquement.</li>
 * </ul>
 */
@Component
public class OperationReadAccess {

    private final AuthentificationHelper authentificationHelper;

    private final CommuneResolver communeResolver;

    private final OperationCustomDao operationCustomDao;

    public OperationReadAccess(AuthentificationHelper authentificationHelper, CommuneResolver communeResolver,
                               OperationCustomDao operationCustomDao) {
        this.authentificationHelper = authentificationHelper;
        this.communeResolver = communeResolver;
        this.operationCustomDao = operationCustomDao;
    }

    /**
     * @return la règle de visibilité de l'utilisateur connecté
     */
    public OperationVisibilityFilter getVisibilityFilter() {
        if (authentificationHelper.hasRestreintAccess()) {
            return OperationVisibilityFilter.global();
        }
        return communeResolver.resolveCodeInsee(authentificationHelper.getOrganisation())
                .map(OperationVisibilityFilter::commune)
                .orElseGet(OperationVisibilityFilter::publicOnly);
    }

    /**
     * Prédicat Criteria de visibilité pour l'utilisateur connecté.
     */
    public Predicate toPredicate(Root<OperationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return getVisibilityFilter().toPredicate(root, query, builder);
    }

    /**
     * Contrôle l'accès en lecture à une opération (ou un secteur) par son identifiant.
     *
     * @param operationId identifiant de l'opération ; une opération inexistante n'est pas refusée ici
     * @throws AccessDeniedException si l'opération existe mais n'est pas visible (403)
     */
    public void checkCanRead(long operationId) {
        if (operationCustomDao.isHidden(operationId, getVisibilityFilter())) {
            throw new AccessDeniedException("L'utilisateur n'a pas les droits de consultation de l'opération id = " + operationId);
        }
    }
}
