package pl.aml.bk.clidbdemo.domain.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@ActiveProfiles("test")
class UserServiceIntegrationTest implements WithAssertions {

    @Autowired
    private UserService userService;

    @Test
    void printUserOperations_shouldReturnFormattedOperationsForUser1() {
        // When
        String result = userService.printUserOperations(1);

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
    void printUserOperations_shouldReturnFormattedOperationsForUser2() {
        // When
        String result = userService.printUserOperations(2);

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
    void printUserOperations_shouldReturnEmptyStringForNonExistentUser() {
        // When
        String result = userService.printUserOperations(999);

        // Then
        assertThat(result).isEmpty();
    }
}
