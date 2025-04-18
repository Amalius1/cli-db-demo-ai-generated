package pl.aml.bk.clidbdemo.domain.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.aml.bk.clidbdemo.domain.database.repository.UserEntityRepository;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@ActiveProfiles("test")
class UserServiceIntegrationTest implements WithAssertions {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Test
    @DisplayName("Print user operations by email should return formatted operations for user 1")
    void printUserOperationsByEmail_shouldReturnFormattedOperationsForUser1() {
        // When
        String result = userService.printUserOperationsByEmail("test.user1@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("Operation: Deposit");
        assertThat(result).contains("Operation: Withdrawal");
        assertThat(result).contains("Operation: Transfer");
        assertThat(result).contains("Operation: Purchase");
        assertThat(result).contains("Amount: 100.50");
        assertThat(result).contains("Amount: 50.00");
        assertThat(result).contains("Amount: 200.75");
        assertThat(result).contains("Amount: 75.00");
    }

    @Test
    void printUserOperationsByEmail_shouldReturnFormattedOperationsForUser2() {
        // When
        String result = userService.printUserOperationsByEmail("test.user2@example.com");

        // Then
        assertThat(result).isNotNull();
        assertThat(result).contains("Operation: Deposit");
        assertThat(result).contains("Operation: Top-Up");
        assertThat(result).contains("Operation: Payment");
        assertThat(result).contains("Operation: Refund");
        assertThat(result).contains("Amount: 300.00");
        assertThat(result).contains("Amount: 120.00");
        assertThat(result).contains("Amount: 45.60");
        assertThat(result).contains("Amount: 30.20");
    }

    @Test
    void printUserOperationsByEmail_shouldReturnEmptyStringForNonExistentUser() {
        // When
        String result = userService.printUserOperationsByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

}
