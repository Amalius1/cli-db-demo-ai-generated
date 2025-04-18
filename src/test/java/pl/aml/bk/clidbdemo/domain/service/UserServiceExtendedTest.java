package pl.aml.bk.clidbdemo.domain.service;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.aml.bk.clidbdemo.config.TestConfig;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationType;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;
import pl.aml.bk.clidbdemo.domain.database.repository.OperationEntityRepository;
import pl.aml.bk.clidbdemo.domain.database.repository.UserEntityRepository;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
class UserServiceExtendedTest implements WithAssertions {

    @Autowired
    private UserService userService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private OperationEntityRepository operationEntityRepository;

    @Test
    void findUserByEmail_shouldReturnUserWhenExists() {
        // Given
        String existingEmail = "test.user1@example.com";

        // When
        Optional<UserEntity> result = userService.findUserByEmail(existingEmail);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(existingEmail);
        assertThat(result.get().getFirstName()).isEqualTo("Test");
        assertThat(result.get().getLastName()).isEqualTo("User1");
    }

    @Test
    void findUserByEmail_shouldReturnEmptyWhenUserDoesNotExist() {
        // Given
        String nonExistingEmail = "nonexistent@example.com";

        // When
        Optional<UserEntity> result = userService.findUserByEmail(nonExistingEmail);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void createUser_shouldCreateAndReturnNewUser() {
        // Given
        String email = "new.user@example.com";
        String firstName = "New";
        String lastName = "User";

        // When
        UserEntity result = userService.createUser(email, firstName, lastName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);

        // Verify the user was saved to the repository
        Optional<UserEntity> savedUser = userEntityRepository.findByEmail(email);
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getId()).isEqualTo(result.getId());
    }

    @Test
    void addOperation_shouldAddOperationToUser() {
        // Given
        UserEntity user = userEntityRepository.findByEmail("test.user1@example.com").orElseThrow();
        OperationType operationType = OperationType.DEPOSIT;
        BigDecimal amount = new BigDecimal("100.00");

        // When
        OperationEntity result = userService.addOperation(user, operationType, amount);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getOperationName()).isEqualTo(operationType);
        assertThat(result.getAmount()).isEqualByComparingTo(amount);
        assertThat(result.getUser().getId()).isEqualTo(user.getId());

        // Verify the operation was saved to the repository
        Optional<OperationEntity> savedOperation = operationEntityRepository.findById(result.getId());
        assertThat(savedOperation).isPresent();
        assertThat(savedOperation.get().getOperationName()).isEqualTo(operationType);
        assertThat(savedOperation.get().getAmount()).isEqualByComparingTo(amount);
        assertThat(savedOperation.get().getUser().getId()).isEqualTo(user.getId());
    }
}
