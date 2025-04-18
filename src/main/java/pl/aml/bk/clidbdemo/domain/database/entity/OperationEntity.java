package pl.aml.bk.clidbdemo.domain.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "operations")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OperationEntity {

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_name")
    private OperationType operationName;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
