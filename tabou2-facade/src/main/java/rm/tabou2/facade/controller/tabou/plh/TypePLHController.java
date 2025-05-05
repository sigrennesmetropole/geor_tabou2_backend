package rm.tabou2.facade.controller.tabou.plh;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypePlhApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypePLH;
import rm.tabou2.service.tabou.plh.PLHService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;
import rm.tabou2.storage.tabou.item.TypePLHCriteria;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class TypePLHController implements TypePlhApi {

	private final PLHService plhService;

	@Override
	public ResponseEntity<TypePLH> createTypePLH(TypePLH typePLH) throws Exception {
		return ResponseEntity.ok(plhService.createTypePLH(typePLH));
	}

	@Override
	public ResponseEntity<TypePLH> createTypePLHWithParent(Long typePLHId, TypePLH typePLH) throws Exception {
		return ResponseEntity.ok(plhService.createTypePLHWithParent(typePLH, typePLHId));
	}

	@Override
	public ResponseEntity<Void> deleteTypePLH(Long typePLHId) throws Exception {
		plhService.deleteTypePLH(typePLHId);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<TypePLH> getTypePLH(Long typePLHId) throws Exception {
		return ResponseEntity.ok(plhService.getTypePLH(typePLHId));
	}

	@Override
	public ResponseEntity<TypePLH> searchTypePLHParent(Long typePLHId) throws Exception {
		return ResponseEntity.ok(plhService.searchTypePLHParent(typePLHId));
	}

	@Override
	public ResponseEntity<PageResult> searchTypePLHs(Long programmeId, String libelle, Date dateDebut, Date dateFin, Integer start,
			Integer resultsNumber, String orderBy, Boolean asc) throws Exception {
		TypePLHCriteria criteria = new TypePLHCriteria();
		criteria.setProgrammeId(programmeId);
		criteria.setLibelle(libelle);
		criteria.setDateDebut(dateDebut);
		criteria.setDateFin(dateFin);

		// Pour le moment on ne laisse que la possibilité de récupérer les éléments sélectionnables
		criteria.setSelectionnable(true);

		if (orderBy == null) {
			orderBy = "order";
		}

		Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypePLHEntity.class);

		Page<TypePLH> typePLHS = plhService.searchTypePLHs(criteria, pageable);

		return ResponseEntity.ok(PaginationUtils.buildPageResult(typePLHS));
	}

	@Override
	public ResponseEntity<TypePLH> updateTypePLH(TypePLH typePLH) throws Exception {
		return ResponseEntity.ok(plhService.updateTypePLH(typePLH));
	}
}
