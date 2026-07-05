package net.bdcc.ormjpa;

import net.bdcc.ormjpa.entities.Product;
import net.bdcc.ormjpa.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class OrmJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrmJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            Product p1 = new Product();
            p1.setName("Computer");
            p1.setPrice(4500);
            p1.setQuantity(10);
            productRepository.save(p1);

            Product p2 = new Product(null, "Printer", 1100, 5);
            productRepository.save(p2);

            Product p3 = Product.builder()
                    .name("SmartPhone").price(200).quantity(25)
                    .build();
            productRepository.save(p3);

            List<Product> products = productRepository.findAll();
            products.forEach(prod -> System.out.println(prod.toString()));
        };
    }
}
