package rm.tabou2.service.tabou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.helper.AuthentificationHelper;
import rm.tabou2.service.helper.evenement.TypeEvenementRigthsHelper;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeEvenementRightsHelperTest {

    @Autowired
    private TypeEvenementRigthsHelper typeEvenementRigthsHelper;

    @MockBean
    private AuthentificationHelper authentificationHelper;

    @DisplayName("testCannotGetTypeEvenementSysteme: Test de l'interdiction de la récupération " +
            "d'un type d'événement système")
    @Test
    void testCannotGetTypeEvenementSysteme() {

        Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(false);

        TypeEvenementEntity typeEvenementEntity = new TypeEvenementEntity();
        typeEvenementEntity.setLibelle("typeEvenement");
        typeEvenementEntity.setCode("code");
        typeEvenementEntity.setSysteme(true);

        Mockito.when(authentificationHelper.hasReferentRole()).thenReturn(false);
        Assertions.assertFalse(typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity));

        Mockito.when(authentificationHelper.hasContributeurRole()).thenReturn(false);
        Assertions.assertFalse(typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity));

        Mockito.when(authentificationHelper.hasConsultationRole()).thenReturn(false);
        Assertions.assertFalse(typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity));
    }

    @Test
    void testGetTypeEvenementSysteme() {
        TypeEvenementEntity typeEvenementEntity = new TypeEvenementEntity();
        typeEvenementEntity.setLibelle("typeEvenement");
        typeEvenementEntity.setCode("code");
        typeEvenementEntity.setSysteme(true);

        Mockito.when(authentificationHelper.hasAdministratorRole()).thenReturn(true);
        Assertions.assertTrue(typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity));
    }
}
