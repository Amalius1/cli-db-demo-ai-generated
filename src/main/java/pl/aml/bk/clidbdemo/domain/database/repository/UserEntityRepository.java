package pl.aml.bk.clidbdemo.domain.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.aml.bk.clidbdemo.domain.database.entity.UserEntity;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);
}
