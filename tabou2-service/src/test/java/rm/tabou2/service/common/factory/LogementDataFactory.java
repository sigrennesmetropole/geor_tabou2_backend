package rm.tabou2.service.common.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rm.tabou2.service.dto.TypeAccessionLogement;
import rm.tabou2.service.dto.TypeLogement;
import rm.tabou2.service.tabou.logement.TypeAccessionLogementService;
import rm.tabou2.service.tabou.logement.TypeLogementService;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Factory de création d'objets de test liés aux logements (types de logement, types d'accession).
 * Mutualisée entre les différents tests.
 */
@Component
@RequiredArgsConstructor
public class LogementDataFactory {

    private final TypeLogementService typeLogementService;
    private final TypeAccessionLogementService typeAccessionLogementService;

    /**
     * Construit un DTO TypeLogement sans le persister.
     */
    public TypeLogement buildTypeLogement(String code, String libelle, int ordre) {
        TypeLogement dto = new TypeLogement();
        dto.setCode(code);
        dto.setLibelle(libelle);
        dto.setOrdre(ordre);
        dto.setDateDebut(OffsetDateTime.now());
        return dto;
    }

    /**
     * Construit et persiste un TypeLogement avec des valeurs par défaut.
     */
    public TypeLogement createTypeLogement(String code, String libelle) {
        TypeLogement dto = buildTypeLogement(code, libelle, 1);
        dto.setDateDebut(OffsetDateTime.now().minusDays(30));
        return typeLogementService.createTypeLogement(dto);
    }

    /**
     * Construit et persiste un TypeLogement avec les valeurs par défaut "T2".
     */
    public TypeLogement createDefaultTypeLogement() {
        return createTypeLogement("T2", "T2");
    }

    /**
     * Construit un DTO TypeAccessionLogement sans le persister.
     */
    public TypeAccessionLogement buildTypeAccessionLogement(String code, String libelle, int ordre,
                                                            List<TypeAccessionLogement.PorteesEnum> portees) {
        TypeAccessionLogement dto = new TypeAccessionLogement();
        dto.setCode(code);
        dto.setLibelle(libelle);
        dto.setOrdre(ordre);
        dto.setDateDebut(OffsetDateTime.now());
        dto.setPortees(portees);
        return dto;
    }

    /**
     * Construit un DTO TypeAccessionLogement sans le persister (portée PROGRAMME par défaut).
     */
    public TypeAccessionLogement buildTypeAccessionLogement(String code, String libelle, int ordre) {
        return buildTypeAccessionLogement(code, libelle, ordre, List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));
    }

    /**
     * Construit et persiste un TypeAccessionLogement.
     */
    public TypeAccessionLogement createTypeAccessionLogement(String code, String libelle,
                                                              List<TypeAccessionLogement.PorteesEnum> portees) {
        TypeAccessionLogement dto = buildTypeAccessionLogement(code, libelle, 1, portees);
        dto.setDateDebut(OffsetDateTime.now().minusDays(30));
        return typeAccessionLogementService.createTypeAccessionLogement(dto);
    }

    /**
     * Construit et persiste un TypeAccessionLogement avec les valeurs par défaut pour les tests opération.
     */
    public TypeAccessionLogement createDefaultTypeAccessionLogementForOperation() {
        return createTypeAccessionLogement("LOC_AIDE", "Locatif aidé",
                List.of(TypeAccessionLogement.PorteesEnum.OPERATION));
    }

    /**
     * Construit et persiste un TypeAccessionLogement avec les valeurs par défaut pour les tests programme.
     */
    public TypeAccessionLogement createDefaultTypeAccessionLogementForProgramme() {
        return createTypeAccessionLogement("LOC_AIDE", "Locatif aidé",
                List.of(TypeAccessionLogement.PorteesEnum.PROGRAMME));
    }
}

