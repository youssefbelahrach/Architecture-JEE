package ma.enset.productsapp;

import ma.enset.productsapp.entities.Product;
import ma.enset.productsapp.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ProductsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsAppApplication.class, args);
    }

    // Ce Bean s'execute automatiquement au demarrage de l'application.
    // Il permet de tester les operations de gestion des produits.
    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository) {
        return args -> {

            // 1) Ajouter des produits
            System.out.println("======= AJOUT DE PRODUITS =======");
            productRepository.save(Product.builder()
                    .name("Ordinateur").price(15000).quantity(9).build());
            productRepository.save(Product.builder()
                    .name("Imprimante").price(1200).quantity(5).build());
            productRepository.save(Product.builder()
                    .name("Smartphone").price(9000).quantity(11).build());
            productRepository.save(Product.builder()
                    .name("Clavier").price(300).quantity(20).build());

            // 2) Consulter tous les produits
            System.out.println("======= LISTE DE TOUS LES PRODUITS =======");
            List<Product> products = productRepository.findAll();
            products.forEach(p -> System.out.println(p.toString()));

            // 3) Consulter un produit par son id
            System.out.println("======= CONSULTER LE PRODUIT ID=1 =======");
            Product product = productRepository.findById(1L).orElse(null);
            System.out.println(product);

            // 4) Chercher des produits (par mot cle dans le nom)
            System.out.println("======= RECHERCHE : produits contenant 'r' =======");
            List<Product> searchResult = productRepository.findByNameContains("r");
            searchResult.forEach(p -> System.out.println(p.toString()));

            // 5) Mettre a jour un produit
            System.out.println("======= MISE A JOUR DU PRODUIT ID=1 =======");
            if (product != null) {
                product.setPrice(20000);      // nouveau prix
                product.setQuantity(7);       // nouvelle quantite
                productRepository.save(product); // save() = insert OU update
                System.out.println("Produit mis a jour : " + productRepository.findById(1L).orElse(null));
            }

            // 6) Supprimer un produit
            System.out.println("======= SUPPRESSION DU PRODUIT ID=4 =======");
            productRepository.deleteById(4L);
            System.out.println("Liste apres suppression :");
            productRepository.findAll().forEach(p -> System.out.println(p.toString()));
        };
    }
}
