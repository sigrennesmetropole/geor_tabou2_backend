package rm.tabou2.service.tabou;

import java.util.List;

import javax.validation.ConstraintViolationException;

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
import rm.tabou2.service.dto.TypeDocument;
import rm.tabou2.service.exception.AppServiceException;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.tabou.document.TypeDocumentService;
import rm.tabou2.storage.tabou.dao.document.TypeDocumentDao;
import rm.tabou2.storage.tabou.entity.document.TypeDocumentEntity;
import rm.tabou2.storage.tabou.item.TypeDocumentCriteria;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeDocumentServiceTest implements ExceptionTest {

    @Autowired
    private TypeDocumentDao typeDocumentDao;

    @Autowired
    private TypeDocumentService typeDocumentService;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(authentificationHelper.hasEditAccess()).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        typeDocumentDao.deleteAll();
    }

    @DisplayName("testSearchTypeDocument: Test de la recherche de types de document")
    @Test
    void testSearchTypeDocument() throws AppServiceException {

        TypeDocument typeDocument1 = new TypeDocument();
        typeDocument1.setLibelle("typeDocument1");

        TypeDocument typeDocument2 = new TypeDocument();
        typeDocument2.setLibelle("typeDocument1");

        TypeDocument typeDocument3 = new TypeDocument();
        typeDocument3.setLibelle("typeDocument3");

        typeDocumentService.createTypeDocument(typeDocument1);
        typeDocumentService.createTypeDocument(typeDocument2);
        typeDocumentService.createTypeDocument(typeDocument3);

        TypeDocumentCriteria typeDocumentCriteria = new TypeDocumentCriteria();
        typeDocumentCriteria.setLibelle("typeDocument1");

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "libelle"));
        Page<TypeDocument> page = typeDocumentService.searchTypeDocument(typeDocumentCriteria, pageable);

        Assertions.assertNotNull(page.getContent());
        Assertions.assertEquals(2, page.getTotalElements());
    }

    @DisplayName("testInactivateTypeDocument: Test de l'inactivation d'un type de document")
    @Test
    void testInactivateTypeDocument() throws AppServiceException {

        TypeDocumentEntity typeDocumentEntity = new TypeDocumentEntity();
        typeDocumentEntity.setLibelle("type doc");

        typeDocumentEntity = typeDocumentDao.save(typeDocumentEntity);

        TypeDocument typeDocument = typeDocumentService.inactivateTypeDocument(typeDocumentEntity.getId());

        Assertions.assertNotNull(typeDocument.getDateInactif());
    }

    @DisplayName("testCannotCreateTypeDocumentWithInvalidParameters: Test de l'interdiction de la création d'un type de document " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotCreateTypeDocumentWithInvalidParameters() {

        final TypeDocument typeDocument = new TypeDocument();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> typeDocumentService.createTypeDocument(typeDocument)
        );

        testConstraintViolationException(constraintViolationException, List.of("libelle"));

    }

    @DisplayName("testCannotUpdateTypeDocumentWithInvalidParameters: Test de l'interdiction de la mise à jour d'un type de document " +
            "avec des paramètres obligatoires non présents")
    @Test
    void testCannotUpdateTypeDocumentWithInvalidParameters() {

        final TypeDocument typeDocument = new TypeDocument();

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(
                ConstraintViolationException.class,
                () -> typeDocumentService.updateTypeDocument(typeDocument)
        );

        testConstraintViolationException(constraintViolationException, List.of("id", "libelle"));

    }

}
