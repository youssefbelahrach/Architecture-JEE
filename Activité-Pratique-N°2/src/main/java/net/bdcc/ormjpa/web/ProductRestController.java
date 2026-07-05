package net.bdcc.ormjpa.web;

import net.bdcc.ormjpa.entities.Product;
import net.bdcc.ormjpa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRestController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> showAllProducts(){
        return productRepository.findAll();
    }

    @GetMapping("/products/{id}")
    public Product findProductById(@PathVariable Long id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found !"));
    }

    @GetMapping("/find/products")
    public List<Product> findByName(@RequestParam(value = "kw", defaultValue = "") String keyword){
        return productRepository.findByNameContainsIgnoreCase(keyword);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProductById(@PathVariable Long id){
        productRepository.deleteById(id);
    }

    @PostMapping("/products")
    public  Product addProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

}
