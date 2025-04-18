package pl.aml.bk.clidbdemo.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationType;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;
import pl.aml.bk.clidbdemo.domain.service.UserService;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

@ShellComponent
public class AddUserOperationCommand {

    private final UserService userService;
    private final Scanner scanner;

    @Autowired
    public AddUserOperationCommand(UserService userService) {
        this(userService, new Scanner(System.in));
    }

    // Constructor for testing
    public AddUserOperationCommand(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }

    // Bean for creating a Scanner with System.in
    @Bean
    public static Scanner systemInScanner() {
        return new Scanner(System.in);
    }

    @ShellMethod(value = "add-user-operation")
    public String addUserOperation() {
        // Get user email
        System.out.print("Enter user email: ");
        String email = scanner.nextLine().trim();

        // Find user by email or create a new one
        UserEntity user = findOrCreateUser(email);

        // Get operation type
        OperationType operationType = getOperationType();

        // Get amount
        BigDecimal amount = getAmount();

        // Add operation
        userService.addOperation(user, operationType, amount);

        return "Operation added successfully for user: " + user.getEmail();
    }

    private UserEntity findOrCreateUser(String email) {
        Optional<UserEntity> existingUser = userService.findUserByEmail(email);

        if (existingUser.isPresent()) {
            System.out.println("User found: " + existingUser.get().getFirstName() + " " + existingUser.get().getLastName());
            return existingUser.get();
        } else {
            System.out.println("User not found. Creating a new user.");
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine().trim();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine().trim();

            return userService.createUser(email, firstName, lastName);
        }
    }

    private OperationType getOperationType() {
        while (true) {
            System.out.println("Available operation types:");
            Arrays.stream(OperationType.values())
                    .forEach(type -> System.out.println("- " + type.getValue()));

            System.out.print("Enter operation type: ");
            String input = scanner.nextLine().trim();

            try {
                return OperationType.fromValue(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid operation type. Please try again.");
            }
        }
    }

    private BigDecimal getAmount() {
        while (true) {
            System.out.print("Enter amount: ");
            String input = scanner.nextLine().trim();

            try {
                BigDecimal amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Amount must be greater than zero. Please try again.");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a valid number.");
            }
        }
    }
}
