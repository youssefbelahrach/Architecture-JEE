package ma.enset.digitalbanking.repositories;

import ma.enset.digitalbanking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Accès aux données des comptes.
 * La clé primaire est de type String (l'UUID du compte).
 */
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
