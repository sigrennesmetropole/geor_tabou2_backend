package rm.tabou2.service.tabou;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.Evenement;
import rm.tabou2.service.helper.programme.EvenementProgrammeRigthsHelper;
import rm.tabou2.service.helper.programme.ProgrammeRightsHelper;
import rm.tabou2.service.mapper.tabou.programme.ProgrammeMapper;
import rm.tabou2.storage.tabou.dao.programme.ProgrammeDao;
import rm.tabou2.storage.tabou.entity.programme.ProgrammeEntity;


@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
class EvenementProgrammeRightsHelperTest {

    @Autowired
    private ProgrammeDao programmeDao;

    @Autowired
    private ProgrammeMapper programmeMapper;

    @Autowired
    private EvenementProgrammeRigthsHelper evenementProgrammeRigthsHelper;

    @MockBean
    private ProgrammeRightsHelper programmeRightsHelper;

    @BeforeEach
    public void initTest() {
        Mockito.when(programmeRightsHelper.checkCanUpdateProgramme(Mockito.any(), Mockito.anyBoolean())).thenReturn(true);
    }

    @AfterEach
    public void afterTest() {
        programmeDao.deleteAll();
    }

    @DisplayName("testCanUpdateEvenementProgramme: test pour vérifier qu'un événement ne peut être modifié que s'il n'est pas un événement systeme")
    @Test
    void testCanUpdateEvenementProgramme() {
        ProgrammeEntity programmeEntity = new ProgrammeEntity();
        programmeEntity.setDiffusionRestreinte(true);
        programmeEntity = programmeDao.save(programmeEntity);

        Evenement actualEvenement = new Evenement();
        actualEvenement.setSysteme(false);

        Assertions.assertTrue(evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(programmeMapper.entityToDto(programmeEntity), actualEvenement));

        actualEvenement = new Evenement();
        actualEvenement.setSysteme(true);

        Assertions.assertFalse(evenementProgrammeRigthsHelper.checkCanUpdateEvenementProgramme(programmeMapper.entityToDto(programmeEntity), actualEvenement));

    }
}
