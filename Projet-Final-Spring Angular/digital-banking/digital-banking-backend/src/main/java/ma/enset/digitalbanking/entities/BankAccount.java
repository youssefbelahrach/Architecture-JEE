package ma.enset.digitalbanking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.digitalbanking.enums.AccountStatus;

import java.util.Date;
import java.util.List;

/**
 * Classe mère représentant un compte bancaire.
 *
 * Stratégie d'héritage : SINGLE_TABLE
 *   -> toutes les sous-classes (CurrentAccount, SavingAccount) sont stockées
 *      dans une seule table, différenciées par la colonne discriminante "TYPE".
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    /**
     * L'identifiant est une chaîne (UUID) plutôt qu'un entier auto-incrémenté :
     * pratique pour ne pas exposer d'ID séquentiels côté API.
     */
    @Id
    private String id;

    private double balance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    /** Relation N..1 : plusieurs comptes -> un client. */
    @ManyToOne
    private Customer customer;

    /** Relation 1..N : un compte -> plusieurs opérations. */
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;
}
