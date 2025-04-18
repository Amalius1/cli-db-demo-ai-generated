package pl.aml.bk.clidbdemo.domain.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aml.bk.clidbdemo.domain.database.entity.OperationEntity;

import java.util.List;

public interface OperationEntityRepository extends JpaRepository<OperationEntity, Integer> {
    List<OperationEntity> findAllByUser_id(Integer userId);
    List<OperationEntity> findAllByUser_email(String email);
}
