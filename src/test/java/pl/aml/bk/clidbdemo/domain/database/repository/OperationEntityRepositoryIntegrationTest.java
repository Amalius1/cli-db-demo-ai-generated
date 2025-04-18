package pl.aml.bk.clidbdemo.domain.database.repository;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationType;

import java.util.List;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@ActiveProfiles("test")
class OperationEntityRepositoryIntegrationTest implements WithAssertions {

    @Autowired
    private OperationEntityRepository operationEntityRepository;

    @Test
    @DisplayName("Find all operations by user ID should return operations for user 1")
    void findAllByUser_id_shouldReturnOperationsForUser1() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_id(1);

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).hasSize(4);
        assertThat(operations).extracting("operationName")
                .containsExactlyInAnyOrder(
                        OperationType.DEPOSIT,
                        OperationType.WITHDRAWAL,
                        OperationType.TRANSFER,
                        OperationType.PURCHASE
                );
    }

    @Test
    @DisplayName("Find all operations by user ID should return operations for user 2")
    void findAllByUser_id_shouldReturnOperationsForUser2() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_id(2);

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).hasSize(4);
        assertThat(operations).extracting("operationName")
                .containsExactlyInAnyOrder(
                        OperationType.DEPOSIT,
                        OperationType.TOP_UP,
                        OperationType.PAYMENT,
                        OperationType.REFUND
                );
    }

    @Test
    @DisplayName("Find all operations by user ID should return empty list for non-existent user")
    void findAllByUser_id_shouldReturnEmptyListForNonExistentUser() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_id(999);

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).isEmpty();
    }

    @Test
    @DisplayName("Find all operations by user email should return operations for user 1")
    void findAllByUser_email_shouldReturnOperationsForUser1() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_email("test.user1@example.com");

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).hasSize(4);
        assertThat(operations).extracting("operationName")
                .containsExactlyInAnyOrder(
                        OperationType.DEPOSIT,
                        OperationType.WITHDRAWAL,
                        OperationType.TRANSFER,
                        OperationType.PURCHASE
                );
    }

    @Test
    @DisplayName("Find all operations by user email should return operations for user 2")
    void findAllByUser_email_shouldReturnOperationsForUser2() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_email("test.user2@example.com");

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).hasSize(4);
        assertThat(operations).extracting("operationName")
                .containsExactlyInAnyOrder(
                        OperationType.DEPOSIT,
                        OperationType.TOP_UP,
                        OperationType.PAYMENT,
                        OperationType.REFUND
                );
    }

    @Test
    @DisplayName("Find all operations by user email should return empty list for non-existent user")
    void findAllByUser_email_shouldReturnEmptyListForNonExistentUser() {
        // When
        List<OperationEntity> operations = operationEntityRepository.findAllByUser_email("nonexistent@example.com");

        // Then
        assertThat(operations).isNotNull();
        assertThat(operations).isEmpty();
    }
}
