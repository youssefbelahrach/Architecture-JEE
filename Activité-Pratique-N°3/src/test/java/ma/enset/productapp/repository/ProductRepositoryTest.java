package ma.enset.productapp.repository;

import ma.enset.productapp.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Étape 4 : test de la couche DAO avec une base H2 embarquée.
 */
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void saveAndFindById_shouldPersistProduct() {
        Product saved = productRepository.save(
                Product.builder().name("Test Produit").price(199.9).quantity(10).build());

        assertThat(saved.getId()).isNotNull();
        assertThat(productRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void findByNameContainingIgnoreCase_shouldFilterByKeyword() {
        productRepository.save(Product.builder().name("Clavier").price(300).quantity(5).build());
        productRepository.save(Product.builder().name("Souris").price(150).quantity(8).build());
        productRepository.save(Product.builder().name("clavier gamer").price(600).quantity(3).build());

        Page<Product> result =
                productRepository.findByNameContainingIgnoreCase("clav", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveProduct() {
        Product saved = productRepository.save(
                Product.builder().name("A supprimer").price(50).quantity(1).build());

        productRepository.deleteById(saved.getId());

        assertThat(productRepository.findById(saved.getId())).isEmpty();
    }
}
