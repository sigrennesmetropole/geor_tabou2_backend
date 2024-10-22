package rm.tabou2.service.tabou;

import java.util.List;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.common.ExceptionTest;
import rm.tabou2.service.dto.TypeEvenement;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.evenement.TypeEvenementRigthsHelper;
import rm.tabou2.service.tabou.evenement.TypeEvenementService;
import rm.tabou2.storage.tabou.dao.evenement.TypeEvenementDao;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;
import rm.tabou2.storage.tabou.item.TypeEvenementCriteria;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeEvenementServiceTest implements ExceptionTest {

    @Autowired
    private TypeEvenementDao typeEvenementDao;

    @Autowired
    private TypeEvenementService typeEvenementService;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @MockBean
    private TypeEvenementRigthsHelper typeEvenementRigthsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
        Mockito.when(typeEvenementRigthsHelper.canGetTypeEvenement(Mockito.any())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        typeEvenementDao.deleteAll();
    }

    @DisplayName("testSearchTypeDocument: Test de la recherche de types de document")
    @Test
    void testSearchTypeDocument() {

        TypeEvenementEntity typeEvenementEntity1 = new TypeEvenementEntity();
        typeEvenementEntity1.setLibelle("typeEvenement1");
        typeEvenementEntity1.setCode("code1");
        typeEvenementEntity1.setSysteme(false);

        TypeEvenementEntity typeEvenementEntity2 = new TypeEvenementEntity();
        typeEvenementEntity2.setLibelle("typeEvenement1");
        typeEvenementEntity2.setCode("code2");
        typeEvenementEntity2.setSysteme(false);

        TypeEvenementEntity typeEvenementEntity3 = new TypeEvenementEntity();
        typeEvenementEntity3.setLibelle("typeEvenement1");
        typeEvenementEntity3.setCode("code3");
        typeEvenementEntity3.setSysteme(true);

        typeEvenementDao.save(typeEvenementEntity1);
        typeEvenementDao.save(typeEvenementEntity2);
        typeEvenementDao.save(typeEvenementEntity3);

        TypeEvenementCriteria typeEvenementCriteria = new TypeEvenementCriteria();
        typeEvenementCriteria.setLibelle("typeEvenement1");
        typeEvenementCriteria.setSysteme(true);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "libelle"));
        Page<TypeEvenement> page = typeEvenementService.searchTypeEvenement(typeEvenementCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(2, page.getTotalElements());
        Assertions.assertTrue(page.stream().noneMatch(TypeEvenement::getSysteme));
    }

    @DisplayName("testCannotCreateTypeEvenementWithInvalidParameters: Test de l'interdiction de la création d'un type d'événement " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateTypeEvenementWithInvalidParameters() {

        final TypeEvenement typeEvenement = new TypeEvenement();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> typeEvenementService.createTypeEvenement(typeEvenement)
        );

        testConstraintViolationException(constraintViolationException, List.of("libelle", "code"));

    }

    @DisplayName("testInactivateTypeDocument: Test de l'inactivation d'un type de document")
    @Test
    void testInactivateTypeDocument() throws AppServiceException {

        TypeEvenementEntity typeEvenementEntity = new TypeEvenementEntity();
        typeEvenementEntity.setLibelle("typeEvenement");
        typeEvenementEntity.setCode("code");
        typeEvenementEntity.setSysteme(false);
        typeEvenementEntity.setDateInactif(null);

        typeEvenementEntity = typeEvenementDao.save(typeEvenementEntity);

        TypeEvenement typeEvenement = typeEvenementService.inactivateTypeEvenement(typeEvenementEntity.getId());

        Assertions.assertNotNull(typeEvenement.getDateInactif());
    }
}
