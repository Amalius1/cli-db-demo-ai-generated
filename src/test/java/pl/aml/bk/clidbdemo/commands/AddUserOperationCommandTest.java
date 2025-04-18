package pl.aml.bk.clidbdemo.commands;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationType;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;
import pl.aml.bk.clidbdemo.domain.service.UserService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddUserOperationCommandTest implements WithAssertions {

    @Mock
    private UserService userService;

    @Mock
    private UserEntity mockUser;

    @Mock
    private OperationEntity mockOperation;

    private AddUserOperationCommand command;

    @BeforeEach
    void setUp() {
        // Set up default behavior for mocks with lenient stubbing
        Mockito.lenient().when(mockUser.getEmail()).thenReturn("test@example.com");
        Mockito.lenient().when(mockUser.getFirstName()).thenReturn("Test");
        Mockito.lenient().when(mockUser.getLastName()).thenReturn("User");

        Mockito.lenient().when(mockOperation.getUser()).thenReturn(mockUser);
        Mockito.lenient().when(mockOperation.getOperationName()).thenReturn(OperationType.DEPOSIT);
        Mockito.lenient().when(mockOperation.getAmount()).thenReturn(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Add operation with existing user should successfully add the operation")
    void addUserOperation_withExistingUser_shouldAddOperation() {
        // Given
        String input = "test@example.com\nDeposit\n100.00\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        command = new AddUserOperationCommand(userService, scanner);

        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userService.addOperation(eq(mockUser), any(OperationType.class), any(BigDecimal.class)))
                .thenReturn(mockOperation);

        // When
        String result = command.addUserOperation();

        // Then
        assertThat(result).contains("Operation added successfully");
        assertThat(result).contains("test@example.com");

        verify(userService).findUserByEmail("test@example.com");
        verify(userService).addOperation(eq(mockUser), eq(OperationType.DEPOSIT), eq(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Add operation with new user should create the user and add the operation")
    void addUserOperation_withNewUser_shouldCreateUserAndAddOperation() {
        // Given
        String input = "new@example.com\nJohn\nDoe\nDeposit\n200.50\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        command = new AddUserOperationCommand(userService, scanner);

        when(userService.findUserByEmail("new@example.com")).thenReturn(Optional.empty());
        when(userService.createUser("new@example.com", "John", "Doe")).thenReturn(mockUser);
        when(userService.addOperation(eq(mockUser), any(OperationType.class), any(BigDecimal.class)))
                .thenReturn(mockOperation);

        // When
        String result = command.addUserOperation();

        // Then
        assertThat(result).contains("Operation added successfully");
        assertThat(result).contains("test@example.com"); // From the mock user

        verify(userService).findUserByEmail("new@example.com");
        verify(userService).createUser("new@example.com", "John", "Doe");
        verify(userService).addOperation(eq(mockUser), eq(OperationType.DEPOSIT), eq(new BigDecimal("200.50")));
    }

    @Test
    @DisplayName("Add operation with invalid operation type should retry until valid type is provided")
    void addUserOperation_withInvalidOperationType_shouldRetryUntilValid() {
        // Given
        String input = "test@example.com\nInvalid\nDeposit\n100.00\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        command = new AddUserOperationCommand(userService, scanner);

        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userService.addOperation(eq(mockUser), any(OperationType.class), any(BigDecimal.class)))
                .thenReturn(mockOperation);

        // When
        String result = command.addUserOperation();

        // Then
        assertThat(result).contains("Operation added successfully");

        verify(userService).addOperation(eq(mockUser), eq(OperationType.DEPOSIT), eq(new BigDecimal("100.00")));
    }

    @Test
    @DisplayName("Add operation with invalid amount should retry until valid amount is provided")
    void addUserOperation_withInvalidAmount_shouldRetryUntilValid() {
        // Given
        String input = "test@example.com\nDeposit\ninvalid\n-50\n100.00\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        command = new AddUserOperationCommand(userService, scanner);

        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userService.addOperation(eq(mockUser), any(OperationType.class), any(BigDecimal.class)))
                .thenReturn(mockOperation);

        // When
        String result = command.addUserOperation();

        // Then
        assertThat(result).contains("Operation added successfully");

        verify(userService).addOperation(eq(mockUser), eq(OperationType.DEPOSIT), eq(new BigDecimal("100.00")));
    }
}
