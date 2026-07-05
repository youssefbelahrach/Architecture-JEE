package ma.enset.productapp.repository;

import ma.enset.productapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Couche DAO basée sur Spring Data JPA.
 * Hérite du CRUD complet de JpaRepository + pagination et tri.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Recherche paginée des produits dont le nom contient le mot-clé
     * (insensible à la casse). Spring Data génère l'implémentation
     * automatiquement à partir du nom de la méthode.
     */
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
