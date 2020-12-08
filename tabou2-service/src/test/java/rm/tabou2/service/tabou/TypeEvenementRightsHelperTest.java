package rm.tabou2.service.tabou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.helper.evenement.TypeEvenementRigthsHelper;
import rm.tabou2.storage.tabou.entity.evenement.TypeEvenementEntity;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class TypeEvenementRightsHelperTest {

    @Autowired
    private TypeEvenementRigthsHelper typeEvenementRigthsHelper;

    @DisplayName("testCannotGetTypeEvenementSysteme: Test de l'interdiction de la récupération " +
            "d'un type d'événement système")
    @Test
    void testCannotGetTypeEvenementSysteme() {

        TypeEvenementEntity typeEvenementEntity = new TypeEvenementEntity();
        typeEvenementEntity.setLibelle("typeEvenement");
        typeEvenementEntity.setCode("code");
        typeEvenementEntity.setSysteme(true);

        Assertions.assertFalse(typeEvenementRigthsHelper.canGetTypeEvenement(typeEvenementEntity));
    }
}
