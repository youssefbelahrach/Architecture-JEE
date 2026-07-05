package ma.enset.productapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entité JPA représentant un produit.
 * Les annotations de validation (jakarta.validation) sont utilisées
 * lors de la soumission du formulaire d'ajout / d'édition.
 */
@Entity
@Data                 // getters, setters, toString, equals, hashCode (Lombok)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 50, message = "Le nom doit contenir entre 3 et 50 caractères")
    private String name;

    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private double price;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private int quantity;
}
