package rm.tabou2.storage.tabou.dao.operation.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import rm.tabou2.storage.common.impl.AbstractCustomDaoImpl;
import rm.tabou2.storage.tabou.dao.operation.MosCustomDao;
import rm.tabou2.storage.tabou.entity.operation.MosEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationGeometryViewEntity;
import rm.tabou2.storage.tabou.entity.operation.SecteurGeometryViewEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class MosCustomDaoImpl extends AbstractCustomDaoImpl implements MosCustomDao {

	@PersistenceContext(unitName = "tabouPU")
	private EntityManager entityManager;
	private static final String OPERATION_GEOMETRY = "shape";
	private static final String OPERATION_IDTABOU = "idTabou";
	private static final String MOS_GEOM = "geometry";
	private static final String PG_ST_INTERSECTS = "st_intersects";

	/**
	 * Constructs a concatenated string of distinct 'libelle2021' values from the MosEntity records
	 * filtered by the specified operation ID and secteur flag.
	 *
	 * @param operationId the ID of the operation used to filter the entities
	 * @param secteur a flag indicating whether to filter by sector-specific or operation-specific logic
	 * @return a concatenated string of distinct 'libelle2021' values, separated by commas
	 */
	@Override
	public String computeOperationMos(long operationId, boolean secteur) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MosEntity> query = criteriaBuilder.createQuery(MosEntity.class);
		Root<MosEntity> root = query.from(MosEntity.class);

		query.where(buildWhere(operationId, secteur, criteriaBuilder, root, query));

		TypedQuery<MosEntity> typedQuery = entityManager.createQuery(query);

		List<MosEntity> resultList = typedQuery.getResultList();
		Set<String> libelles = Set.of();
		if (CollectionUtils.isNotEmpty(resultList)) {
			libelles = resultList.stream().map(MosEntity::getLibelle2021).collect(Collectors.toSet());
		}
		return String.join(", ", libelles);
	}

	/**
	 * Creates a `where` clause for a query using the provided parameters.
	 * This method builds a boolean expression to evaluate spatial intersection for a specific operation
	 * by determining if the `geom` attribute in `MosEntity` intersects with the geometry
	 * of either `SecteurGeometryViewEntity` or `OperationGeometryViewEntity` based on the context.
	 *
	 * @param operationId the ID of the operation used to filter the entities
	 * @param secteur a flag indicating whether to build a query for sector-based or operation-based logic
	 * @param criteriaBuilder the CriteriaBuilder instance used to construct the query predicates
	 * @param root the root entity of the main query for constructing paths
	 * @param query the main CriteriaQuery that references subqueries for filtering
	 * @return an expression representing the spatial intersection condition
	 */
	private Expression<Boolean> buildWhere(long operationId, boolean secteur, CriteriaBuilder criteriaBuilder,
			Root<MosEntity> root, CriteriaQuery<MosEntity> query) {
		if (secteur) {
			Subquery<SecteurGeometryViewEntity> subquery = query.subquery(SecteurGeometryViewEntity.class);
			Root<SecteurGeometryViewEntity> subroot = subquery.from(SecteurGeometryViewEntity.class);

			subquery.select(subroot.get(OPERATION_GEOMETRY));
			subquery.where(criteriaBuilder.equal(subroot.get(OPERATION_IDTABOU), criteriaBuilder.literal(operationId)));

			return criteriaBuilder.isTrue(criteriaBuilder.function(PG_ST_INTERSECTS, Boolean.class, subquery, root.get(MOS_GEOM)));
		} else {
			Subquery<OperationGeometryViewEntity> subquery = query.subquery(OperationGeometryViewEntity.class);
			Root<OperationGeometryViewEntity> subroot = subquery.from(OperationGeometryViewEntity.class);

			subquery.select(subroot.get(OPERATION_GEOMETRY));
			subquery.where(criteriaBuilder.equal(subroot.get(OPERATION_IDTABOU), criteriaBuilder.literal(operationId)));

			return criteriaBuilder.isTrue(criteriaBuilder.function(PG_ST_INTERSECTS, Boolean.class, subquery, root.get(MOS_GEOM)));
		}
	}
}
