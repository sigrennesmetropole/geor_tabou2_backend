package rm.tabou2.facade.controller.tabou.logement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import rm.tabou2.facade.api.TypesAccessionLogementApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeAccessionLogement;
import rm.tabou2.service.tabou.logement.TypeAccessionLogementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.entity.logement.TypeAccessionLogementEntity;

@RestController
@RequiredArgsConstructor
public class TypesAccessionLogementApiController implements TypesAccessionLogementApi {

    private final TypeAccessionLogementService typeAccessionLogementService;

    @Override
    public ResponseEntity<TypeAccessionLogement> createTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement) {
        return new ResponseEntity<>(typeAccessionLogementService.createTypeAccessionLogement(typeAccessionLogement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeAccessionLogement> updateTypeAccessionLogement(TypeAccessionLogement typeAccessionLogement) {
        return new ResponseEntity<>(typeAccessionLogementService.updateTypeAccessionLogement(typeAccessionLogement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTypeAccessionLogements(String libelle, Boolean actifUniquement, Integer start, Integer resultsNumber, String orderBy, Boolean asc) {

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeAccessionLogementEntity.class);

        Page<TypeAccessionLogement> page = typeAccessionLogementService.searchTypeAccessionLogements(libelle, actifUniquement, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeAccessionLogement> getTypeAccessionLogementById(Long typeAccessionLogementId) {
        return new ResponseEntity<>(typeAccessionLogementService.getById(typeAccessionLogementId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeAccessionLogement> inactivateTypeAccessionLogement(Long typeAccessionLogementId) {
        return new ResponseEntity<>(typeAccessionLogementService.inactivateTypeAccessionLogement(typeAccessionLogementId), HttpStatus.OK);
    }
}

