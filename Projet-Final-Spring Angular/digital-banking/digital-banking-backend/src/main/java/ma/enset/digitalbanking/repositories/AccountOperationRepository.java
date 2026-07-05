package ma.enset.digitalbanking.repositories;

import ma.enset.digitalbanking.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux données des opérations.
 */
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    /** Toutes les opérations d'un compte donné. */
    List<AccountOperation> findByBankAccountId(String accountId);

    /**
     * Version paginée : indispensable pour l'historique du compte,
     * qui peut contenir des centaines d'opérations.
     */
    Page<AccountOperation> findByBankAccountId(String accountId, Pageable pageable);
}
