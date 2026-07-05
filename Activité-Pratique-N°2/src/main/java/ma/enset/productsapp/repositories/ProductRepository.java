package ma.enset.productsapp.repositories;

import ma.enset.productsapp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Requete derivee : Spring Data genere automatiquement le SQL
    // a partir du nom de la methode
    List<Product> findByNameContains(String keyword);

    // Meme resultat en utilisant JPQL explicitement
    @Query("select p from Product p where p.name like :x")
    List<Product> search(@Param("x") String keyword);
}
