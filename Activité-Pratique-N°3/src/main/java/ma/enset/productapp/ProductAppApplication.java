package ma.enset.productapp;

import ma.enset.productapp.entities.Product;
import ma.enset.productapp.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAppApplication.class, args);
    }

    /**
     * Étape 4 : test de la couche DAO.
     * Insère quelques produits au démarrage puis les affiche dans la console.
     * (À supprimer ou commenter en production.)
     */
    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(Product.builder().name("Ordinateur portable").price(9500).quantity(12).build());
                productRepository.save(Product.builder().name("Imprimante laser").price(1200).quantity(30).build());
                productRepository.save(Product.builder().name("Smartphone").price(4500).quantity(50).build());
                productRepository.save(Product.builder().name("Clavier mécanique").price(650).quantity(80).build());
                productRepository.save(Product.builder().name("Écran 27 pouces").price(2300).quantity(20).build());
                productRepository.save(Product.builder().name("Disque SSD 1To").price(890).quantity(100).build());
                productRepository.save(Product.builder().name("Souris sans fil").price(180).quantity(150).build());
                productRepository.save(Product.builder().name("Casque audio").price(750).quantity(60).build());
            }

            System.out.println("=========== Test de la couche DAO ===========");
            productRepository.findAll().forEach(p ->
                    System.out.println(p.getId() + " | " + p.getName() + " | " + p.getPrice() + " | " + p.getQuantity()));
            System.out.println("=============================================");
        };
    }
}
