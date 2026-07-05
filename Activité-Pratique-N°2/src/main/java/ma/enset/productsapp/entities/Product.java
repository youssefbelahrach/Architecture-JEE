package ma.enset.productsapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data                 // genere getters, setters, equals, hashCode, toString
@NoArgsConstructor    // constructeur sans arguments (obligatoire pour JPA)
@AllArgsConstructor   // constructeur avec tous les arguments
@Builder              // pattern Builder pour creer des objets facilement
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;
    private int quantity;
}
