package rm.tabou2.facade.controller.tabou.evenement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rm.tabou2.facade.api.TypesEvenementsApi;
import rm.tabou2.service.dto.PageResult;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.tabou.evenement.TypeEvenementService;
import rm.tabou2.service.utils.PaginationUtils;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;

import javax.validation.Valid;
import java.util.Date;

public class TypesEvenementsApiController implements TypesEvenementsApi {

    @Autowired
    private TypeEvenementService typeEvenementService;

    @Override
    public ResponseEntity<TypeEvenement> getTypeEvenementById(@Valid Long typeEvenementId) throws Exception {
        return new ResponseEntity<>(typeEvenementService.getTypeEvenementById(typeEvenementId), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<TypeEvenement> createTypeEvenements(@Valid TypeEvenement typeEvenement) throws Exception {
        return new ResponseEntity<>(typeEvenementService.createTypeEvenement(typeEvenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TypeEvenement> updateTypeEvenements(@Valid TypeEvenement typeEvenement) throws Exception {
        return new ResponseEntity<>(typeEvenementService.updateTypeEvenement(typeEvenement), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PageResult> searchTypesEvenements(@Valid String libelle, @Valid Boolean inactif, @Valid Integer start, @Valid Integer resultsNumber, @Valid String orderBy, @Valid Boolean asc) throws Exception {
        TypeEvenementCriteria typeEvenementCriteria = new TypeEvenementCriteria();
        typeEvenementCriteria.setLibelle(libelle);
        typeEvenementCriteria.setInactif(inactif);

        Pageable pageable = PaginationUtils.buildPageable(start, resultsNumber, orderBy, asc, TypeEvenement.class);

        Page<TypeEvenement> page = typeEvenementService.searchTypeEvenement(typeEvenementCriteria, pageable);

        return new ResponseEntity<>(PaginationUtils.buildPageResult(page), HttpStatus.OK);

    }


    @Override
    public ResponseEntity<TypeEvenement> inactivateTypeEvenement(Long typeEvenementId) throws Exception {
        return new ResponseEntity<>(typeEvenementService.inactivateTypeEvenement(typeEvenementId), HttpStatus.OK);
    }

}
