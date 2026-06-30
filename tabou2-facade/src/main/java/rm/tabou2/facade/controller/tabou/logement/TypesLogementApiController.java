package rm.tabou2.facade.controller.tabou.logement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesLogementApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.service.tabou.logement.TypeLogementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.logement.TypeLogementEntity;

@RestController
@RequiredArgsConstructor
public class TypesLogementApiController implements TypesLogementApi {

    private final TypeLogementService typeLogementService;

    @Override
    public ResponseEntity<TypeLogement> createTypeLogement(TypeLogement typeLogement) {
        return new ResponseEntity<>(typeLogementService.createTypeLogement(typeLogement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeLogement> updateTypeLogement(TypeLogement typeLogement) {
        return new ResponseEntity<>(typeLogementService.updateTypeLogement(typeLogement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTypeLogements(String libelle, Boolean actifUniquement, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeLogementEntity.class);

        Page<TypeLogement> page = typeLogementService.searchTypeLogements(libelle, actifUniquement, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeLogement> getTypeLogementById(Long typeLogementId) {
        return new ResponseEntity<>(typeLogementService.getById(typeLogementId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeLogement> inactivateTypeLogement(Long typeLogementId) {
        return new ResponseEntity<>(typeLogementService.inactivateTypeLogement(typeLogementId), HttpStatus.OK);
    }
}

