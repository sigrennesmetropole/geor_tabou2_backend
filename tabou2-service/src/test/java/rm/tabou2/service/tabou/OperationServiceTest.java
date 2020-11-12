package rm.tabou2.service.tabou;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.DatabaseInitializerTest;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.Nature;
import rm.tabou2.service.dto.Operation;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.operation.OperationRightsHelper;
import rm.tabou2.service.mapper.tabou.operation.NatureMapper;
import rm.tabou2.service.tabou.operation.OperationService;
import rm.tabou2.storage.sig.dao.SecteurDao;
import rm.tabou2.storage.sig.entity.SecteurEntity;
import rm.tabou2.storage.tabou.dao.operation.EtapeOperationDao;
import rm.tabou2.storage.tabou.dao.operation.NatureDao;
import rm.tabou2.storage.tabou.dao.operation.OperationDao;
import rm.tabou2.storage.tabou.entity.operation.EtapeOperationEntity;
import rm.tabou2.storage.tabou.entity.operation.NatureEntity;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.item.OperationsCriteria;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class OperationServiceTest extends DatabaseInitializerTest implements ExceptionTest {

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private EtapeOperationDao etapeOperationDao;

    @Autowired
    private NatureDao natureDao;

    @Autowired
    private SecteurDao secteurDao;

    @Autowired
    private NatureMapper natureMapper;

    @Autowired
    private OperationService operationService;

    @MockBean
    private OperationRightsHelper operationRightsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(operationRightsHelper.checkCanGetOperation(Mockito.any())).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanCreateOperation(Mockito.any())).thenReturn(true);
        Mockito.when(operationRightsHelper.checkCanUpdateOperation(Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        operationDao.deleteAll();
        secteurDao.deleteAll();
    }

    @Test
    void testSearchOperation() {

        // enregistrer une operation dans la base temporaire H2
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("test");
        operationEntity.setDiffusionRestreinte(false);
        operationDao.save(operationEntity);


        OperationsCriteria operationsCriteria = new OperationsCriteria();
        operationsCriteria.setNom("tes*");
        operationsCriteria.setDiffusionRestreinte(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nom"));

        Page<Operation> page = operationService.searchOperations(operationsCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals( "test", page.getContent().get(0).getNom());
    }

    @DisplayName("testCannotCreateOperationWithInvalidParameters: Test de l'interdiction de la création d'une opération " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateOperationWithInvalidParameters() {

        final Operation operation = new Operation();
        operation.setDiffusionRestreinte(true);
        operation.setNumAds("numads4");
        operation.setSecteur(true);

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> operationService.createOperation(operation)
        );

        testConstraintViolationException(constraintViolationException, List.of("nom", "code", "nature", "idEmprise"));

    }

    @DisplayName("testCannotUpdateOperationWithInvalidParameters: Test de l'interdiction de la modification d'une opération " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotUpdateOperationWithInvalidParameters() {

        final Operation operation = new Operation();
        operation.setDiffusionRestreinte(true);
        operation.setNumAds("numads4");
        operation.setSecteur(true);

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> operationService.updateOperation(operation)
        );

        testConstraintViolationException(constraintViolationException, List.of("nom", "code", "id", "etape"));

    }

    @DisplayName("testUpdateOperationWithDiffusionRestreinte: Test de l'édition d'une opération " +
            "avec une étape qui change la diffusion restreinte'")
    @Test
    void testUpdateOperationWithDiffusionRestreinte() throws AppServiceException {

        SecteurEntity secteurEntity = new SecteurEntity();
        secteurEntity.setId(1);
        secteurDao.save(secteurEntity);

        NatureEntity natureEntityZAC = natureDao.findByLibelle(Nature.LibelleEnum.ZAC.name());

        Operation operation = new Operation();
        operation.setNom("nom4");
        operation.setDiffusionRestreinte(true);
        operation.setCode("code4");
        operation.setNumAds("numads4");
        operation.setSecteur(true);
        operation.setNature(natureMapper.entityToDto(natureEntityZAC));
        operation.setIdEmprise(secteurEntity.getId().longValue());

        operation = operationService.createOperation(operation);

        SecteurEntity secteurEntityUpdated = secteurDao.findOneById(1);
        Assertions.assertEquals(operation.getId().intValue(), secteurEntityUpdated.getIdTabou());

        EtapeOperationEntity etapeOperationEntity = etapeOperationDao.findByCode("EN_PROJET_PUBLIC");
        long operationId = operation.getId();
        long etapeId = etapeOperationEntity.getId();

        operation = operationService.updateEtapeOfOperationId(operationId, etapeId);

        Assertions.assertFalse(operation.isDiffusionRestreinte());

    }

}
