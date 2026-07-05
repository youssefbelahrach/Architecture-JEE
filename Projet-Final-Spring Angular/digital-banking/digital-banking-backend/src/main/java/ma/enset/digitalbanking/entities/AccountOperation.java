package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.digitalbanking.enums.OperationType;

import java.util.Date;

/**
 * Une opération (DEBIT ou CREDIT) effectuée sur un compte bancaire.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    /** Relation N..1 : plusieurs opérations -> un compte. */
    @ManyToOne
    private BankAccount bankAccount;

    private String description;
}
