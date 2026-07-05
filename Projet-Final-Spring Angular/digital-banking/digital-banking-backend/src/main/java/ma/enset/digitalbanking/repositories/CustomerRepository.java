package ma.enset.digitalbanking.repositories;

import ma.enset.digitalbanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux données des clients.
 * JpaRepository fournit déjà : save, findById, findAll, delete, count...
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Recherche par nom contenant un mot-clé (requête dérivée du nom de méthode).
     * Utile pour la fonctionnalité de recherche côté frontend.
     */
    List<Customer> findByNameContainingIgnoreCase(String keyword);
}
