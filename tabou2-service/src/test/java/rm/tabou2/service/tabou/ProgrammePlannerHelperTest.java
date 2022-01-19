package rm.tabou2.service.tabou;

import org.joda.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import rm.tabou2.service.StarterSpringBootTestApplication;
import rm.tabou2.service.dto.Programme;
import rm.tabou2.service.helper.programme.ProgrammePlannerHelper;
import rm.tabou2.storage.tabou.dao.ddc.PermisConstruireDao;
import rm.tabou2.storage.tabou.entity.ddc.PermisConstruireEntity;
import rm.tabou2.storage.tabou.dao.agapeo.AgapeoDao;
import rm.tabou2.storage.tabou.entity.agapeo.AgapeoEntity;

import java.util.Date;

@RunWith(SpringRunner.class)
@TestPropertySource(value = {"classpath:application.properties"})
@SpringBootTest(classes = StarterSpringBootTestApplication.class)
@Transactional
class ProgrammePlannerHelperTest {

    @Autowired
    private AgapeoDao agapeoDao;

    @Autowired
    private PermisConstruireDao permisConstruireDao;

    @Autowired
    private ProgrammePlannerHelper programmePlannerHelper;

    @DisplayName("testProgrammeSuiviHabitationValues: Test des valeurs de suivi habitation pour les permis " +
            "de construire du programme")
    @Test
    void testProgrammePermisSuiviHabitationValues() {

        LocalDateTime localDateTimeAds1 = new LocalDateTime(2018, 3, 10, 15, 30, 55);
        LocalDateTime localDateTimeAds2 = new LocalDateTime(2018, 3, 10, 15, 0, 0);
        LocalDateTime localDateTimeAds3 = new LocalDateTime(2019, 4, 20, 9, 10, 0);

        LocalDateTime localDateTimeDat1 = new LocalDateTime(2019, 12, 1, 16, 0, 0);
        LocalDateTime localDateTimeDat2 = new LocalDateTime(2020, 11, 2, 15, 0, 0);

        Date docDatePrevu = new Date();

        PermisConstruireEntity permisConstruireEntity1 = new PermisConstruireEntity();
        permisConstruireEntity1.setNumAds("numadspr");
        permisConstruireEntity1.setAdsDate(localDateTimeAds1.toDate());
        permisConstruireEntity1.setDatDate(localDateTimeDat1.toDate());
        permisConstruireEntity1.setDocDate(null);

        PermisConstruireEntity permisConstruireEntity2 = new PermisConstruireEntity();
        permisConstruireEntity2.setNumAds("numadspr");
        permisConstruireEntity2.setAdsDate(localDateTimeAds2.toDate());
        permisConstruireEntity2.setDatDate(localDateTimeDat2.toDate());
        permisConstruireEntity2.setDocDate(null);

        PermisConstruireEntity permisConstruireEntity3 = new PermisConstruireEntity();
        permisConstruireEntity3.setNumAds("numadspr");
        permisConstruireEntity3.setAdsDate(localDateTimeAds3.toDate());
        permisConstruireEntity3.setDatDate(null);
        permisConstruireEntity3.setDocDate(null);

        permisConstruireDao.save(permisConstruireEntity1);
        permisConstruireDao.save(permisConstruireEntity2);
        permisConstruireDao.save(permisConstruireEntity3);

        Programme programme = new Programme();
        programme.setNumAds("numadspr");
        programme.setAdsDatePrevu(null);
        programme.setDatDatePrevu(new Date());
        programme.setDocDatePrevu(docDatePrevu);

        programmePlannerHelper.computePermisSuiviHabitatOfProgramme(programme);

        Assertions.assertEquals(localDateTimeAds2.toDate(), programme.getAdsDate());
        Assertions.assertEquals(docDatePrevu, programme.getDocDate());
        Assertions.assertEquals(localDateTimeDat1.toDate(), programme.getDatDate());
    }

    @DisplayName("testProgrammeAgapeoSuiviHabitationValues: Test des valeurs de suivi habitation pour les agapeo " +
            "du programme")
    @Test
    void testProgrammeAgapeoSuiviHabitationValues() {

        AgapeoEntity agapeoEntity1 = new AgapeoEntity();
        agapeoEntity1.setNumAds("numadspr");
        agapeoEntity1.setLogementsAccessAide(10);
        agapeoEntity1.setLogementsAccessMaitrise(15);
        agapeoEntity1.setLogementsLocatifAide(0);
        agapeoEntity1.setLogementsLocatifReguleHlm(0);
        agapeoEntity1.setLogementsLocatifRegulePrive(12);

        AgapeoEntity agapeoEntity2 = new AgapeoEntity();
        agapeoEntity2.setNumAds("numadspr");
        agapeoEntity2.setLogementsAccessAide(11);
        agapeoEntity2.setLogementsAccessMaitrise(7);
        agapeoEntity2.setLogementsLocatifAide(3);
        agapeoEntity2.setLogementsLocatifReguleHlm(0);
        agapeoEntity2.setLogementsLocatifRegulePrive(40);

        agapeoDao.save(agapeoEntity1);
        agapeoDao.save(agapeoEntity2);

        Programme programme = new Programme();
        programme.setNumAds("numadspr");
        programme.setLogementsAccessAidePrevu(0);
        programme.setLogementsAccessMaitrisePrevu(7);
        programme.setLogementsLocatifAidePrevu(1);
        programme.setLogementsLocatifReguleHlmPrevu(18);
        programme.setLogementsLocatifRegulePrivePrevu(0);

        programmePlannerHelper.computeAgapeoSuiviHabitatOfProgramme(programme);

        Assertions.assertEquals(21, programme.getLogementsAccessAide());
        Assertions.assertEquals(22, programme.getLogementsAccessMaitrise());
        Assertions.assertEquals(3, programme.getLogementsLocatifAide());
        Assertions.assertEquals(18, programme.getLogementsLocatifReguleHLM());
        Assertions.assertEquals(52, programme.getLogementsLocatifRegulePrive());
    }
}
