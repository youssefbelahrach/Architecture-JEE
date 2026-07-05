package net.youssfi.webmvcproducts.web;

import jakarta.validation.Valid;
import net.youssfi.webmvcproducts.entities.Product;
import net.youssfi.webmvcproducts.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller exposing the products API consumed by the Angular front-end.
 * CORS is opened globally so that the Angular dev server (http://localhost:4200)
 * can call this service (http://localhost:8083).
 */
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductRestAPI {

    private final ProductRepository productRepository;

    public ProductRestAPI(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return productRepository.findByNameContainsIgnoreCase(keyword);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Product " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product save(@Valid @RequestBody Product product) {
        product.setId(null); // ensure creation, not update
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody Product product) {
        if (!productRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product " + id + " not found");
        product.setId(id);
        return productRepository.save(product);
    }

    /**
     * Toggles the "selected" flag of a product (used by the checkbox in the UI).
     */
    @PatchMapping("/{id}/select")
    public Product toggleSelected(@PathVariable Long id) {
        Product product = findById(id);
        product.setSelected(!product.isSelected());
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!productRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product " + id + " not found");
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
