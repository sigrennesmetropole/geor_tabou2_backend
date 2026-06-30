package rm.tabou2.service.tabou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import rm.tabou2.service.helper.plh.PLHSynchronizationHelper;
import rm.tabou2.storage.tabou.entity.operation.OperationEntity;
import rm.tabou2.storage.tabou.entity.plh.AttributPLHEntity;
import rm.tabou2.storage.tabou.entity.plh.TypePLHEntity;

import java.util.HashSet;
import java.util.Set;

class PLHSynchronizationHelperTest {

    private PLHSynchronizationHelper plhSynchronizationHelper;

    @BeforeEach
    void setUp() {
        plhSynchronizationHelper = new PLHSynchronizationHelper();
    }

    // ==================== synchronizePLHToOperation ====================

    @DisplayName("synchronizePLHToOperation: without synchronizedField, no operation field should be modified")
    @Test
    void testSynchronizePLHToOperationWithoutSynchronizedField() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, null);
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nomOriginal");

        plhSynchronizationHelper.synchronizePLHToOperation(typePLHEntity, operationEntity);

        Assertions.assertEquals("nomOriginal", operationEntity.getNom());
    }

    @DisplayName("synchronizePLHToOperation: with valid synchronizedField and attribute found, operation field should be updated")
    @Test
    void testSynchronizePLHToOperationWithSynchronizedFieldAndAttributeFound() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, "nom");

        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setType(typePLHEntity);
        attributPLHEntity.setValue("nouveauNom");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAttributsPLH(buildAttributsPLH(attributPLHEntity));

        plhSynchronizationHelper.synchronizePLHToOperation(typePLHEntity, operationEntity);

        Assertions.assertEquals("nouveauNom", operationEntity.getNom());
    }

    @DisplayName("synchronizePLHToOperation: with invalid synchronizedField, no exception should be thrown")
    @Test
    void testSynchronizePLHToOperationWithInvalidSynchronizedField() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, "champInexistant");
        OperationEntity operationEntity = new OperationEntity();

        Assertions.assertDoesNotThrow(
                () -> plhSynchronizationHelper.synchronizePLHToOperation(typePLHEntity, operationEntity));
    }

    @DisplayName("synchronizePLHToOperation: with valid synchronizedField but absent attribute, setter should be called with null")
    @Test
    void testSynchronizePLHToOperationWithSynchronizedFieldAndAbsentAttribute() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, "nom");
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nomOriginal");

        plhSynchronizationHelper.synchronizePLHToOperation(typePLHEntity, operationEntity);

        Assertions.assertNull(operationEntity.getNom());
    }

    @DisplayName("synchronizePLHToOperation: recursion on children, operation field should be updated from child")
    @Test
    void testSynchronizePLHToOperationRecursionOnChildren() {
        TypePLHEntity typePLHParent = buildTypePLHEntity(1L, null);
        TypePLHEntity typePLHChild = buildTypePLHEntity(2L, "nom");
        typePLHParent.setFils(buildChildrenTypePLH(typePLHChild));

        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setType(typePLHChild);
        attributPLHEntity.setValue("nomDepuisFils");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setAttributsPLH(buildAttributsPLH(attributPLHEntity));

        plhSynchronizationHelper.synchronizePLHToOperation(typePLHParent, operationEntity);

        Assertions.assertEquals("nomDepuisFils", operationEntity.getNom());
    }

    // ==================== synchronizeOperationToPLH ====================

    @DisplayName("synchronizeOperationToPLH: without synchronizedField, no PLH value should be modified")
    @Test
    void testSynchronizeOperationToPLHWithoutSynchronizedField() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, null);

        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setType(typePLHEntity);
        attributPLHEntity.setValue("valeurOriginal");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nom");
        operationEntity.setAttributsPLH(buildAttributsPLH(attributPLHEntity));

        plhSynchronizationHelper.synchronizeOperationToPLH(typePLHEntity, operationEntity);

        Assertions.assertEquals("valeurOriginal", attributPLHEntity.getValue());
    }

    @DisplayName("synchronizeOperationToPLH: with valid synchronizedField and attribute found, PLH value should be updated")
    @Test
    void testSynchronizeOperationToPLHWithSynchronizedFieldAndAttributeFound() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, "nom");

        AttributPLHEntity attributPLHEntity = new AttributPLHEntity();
        attributPLHEntity.setType(typePLHEntity);
        attributPLHEntity.setValue("ancienneValeur");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nomDepuisOperation");
        operationEntity.setAttributsPLH(buildAttributsPLH(attributPLHEntity));

        plhSynchronizationHelper.synchronizeOperationToPLH(typePLHEntity, operationEntity);

        Assertions.assertEquals("nomDepuisOperation", attributPLHEntity.getValue());
    }

    @DisplayName("synchronizeOperationToPLH: with invalid synchronizedField, no exception should be thrown")
    @Test
    void testSynchronizeOperationToPLHWithInvalidSynchronizedField() {
        TypePLHEntity typePLHEntity = buildTypePLHEntity(1L, "champInexistant");
        OperationEntity operationEntity = new OperationEntity();

        Assertions.assertDoesNotThrow(
                () -> plhSynchronizationHelper.synchronizeOperationToPLH(typePLHEntity, operationEntity));
    }

    @DisplayName("synchronizeOperationToPLH: recursion on children, child PLH value should be updated")
    @Test
    void testSynchronizeOperationToPLHRecursionOnChildren() {
        TypePLHEntity typePLHParent = buildTypePLHEntity(1L, null);
        TypePLHEntity typePLHChild = buildTypePLHEntity(2L, "nom");
        typePLHParent.setFils(buildChildrenTypePLH(typePLHChild));

        AttributPLHEntity attributPLHChild = new AttributPLHEntity();
        attributPLHChild.setType(typePLHChild);
        attributPLHChild.setValue("ancienneValeur");

        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setNom("nomDepuisOperation");
        operationEntity.setAttributsPLH(buildAttributsPLH(attributPLHChild));

        plhSynchronizationHelper.synchronizeOperationToPLH(typePLHParent, operationEntity);

        Assertions.assertEquals("nomDepuisOperation", attributPLHChild.getValue());
    }

    // ==================== utility methods ====================

    private TypePLHEntity buildTypePLHEntity(long id, String synchronizedField) {
        TypePLHEntity typePLHEntity = new TypePLHEntity();
        typePLHEntity.setId(id);
        typePLHEntity.setSynchronizedField(synchronizedField);
        return typePLHEntity;
    }

    private Set<AttributPLHEntity> buildAttributsPLH(AttributPLHEntity attributPLHEntity) {
        Set<AttributPLHEntity> attributsPLH = new HashSet<>();
        attributsPLH.add(attributPLHEntity);
        return attributsPLH;
    }

    private Set<TypePLHEntity> buildChildrenTypePLH(TypePLHEntity typePLHChild) {
        Set<TypePLHEntity> children = new HashSet<>();
        children.add(typePLHChild);
        return children;
    }
}