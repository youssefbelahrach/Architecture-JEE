package net.youssfi.webmvcproducts.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString @Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 60, message = "Name must be between 3 and 60 characters")
    private String name;

    @Min(value = 0, message = "Price must be positive")
    private double price;

    @Min(value = 0, message = "Quantity must be positive")
    private int quantity;

    private boolean selected;
}
