package pl.aml.bk.clidbdemo.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationType;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;
import pl.aml.bk.clidbdemo.domain.database.repository.OperationEntityRepository;
import pl.aml.bk.clidbdemo.domain.database.repository.UserEntityRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OperationEntityRepository operationEntityRepository;
    private final UserEntityRepository userEntityRepository;


    public String printUserOperationsByEmail(String email) {
        List<OperationEntity> allByUserEmail = operationEntityRepository.findAllByUser_email(email);

        return allByUserEmail.stream()
                .map(x -> "Operation: " + x.getOperationName().getValue() + "\n" +
                        "Amount: " + x.getAmount() + "\n" +
                        "-".repeat(10))
                .collect(Collectors.joining("\n"));
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userEntityRepository.findByEmail(email);
    }

    public UserEntity createUser(String email, String firstName, String lastName) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        return userEntityRepository.save(newUser);
    }

    public OperationEntity addOperation(UserEntity user, OperationType operationType, BigDecimal amount) {
        OperationEntity operation = new OperationEntity();
        operation.setOperationName(operationType);
        operation.setAmount(amount);
        operation.setUser(user);

        return operationEntityRepository.save(operation);
    }
}
