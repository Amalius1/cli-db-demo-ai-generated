package pl.aml.bk.clidbdemo.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;
import pl.aml.bk.clidbdemo.domain.database.repository.OperationEntityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {


    private final OperationEntityRepository operationEntityRepository;

    public String printUserOperations(Integer id) {

        List<OperationEntity> allByUserId = operationEntityRepository.findAllByUser_id(id);

        return allByUserId.stream()
                .map(x -> "Operation: " + x.getOperationName().getValue() + "\n" +
                        "Amount: " + x.getAmount() + "\n" +
                        "-".repeat(10))
                .collect(Collectors.joining("\n"));
    }

}
