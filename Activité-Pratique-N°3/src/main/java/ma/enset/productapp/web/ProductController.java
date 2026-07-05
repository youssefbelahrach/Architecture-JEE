package ma.enset.productapp.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.productapp.entities.Product;
import ma.enset.productapp.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.IntStream;

/**
 * Contrôleur Spring MVC : gère l'affichage, l'ajout, la recherche,
 * l'édition, la mise à jour et la suppression des produits.
 */
@Controller
@AllArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    /**
     * Liste des produits avec pagination + recherche par mot-clé.
     */
    @GetMapping("/user/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Product> pageProducts =
                productRepository.findByNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
        model.addAttribute("productList", pageProducts.getContent());
        model.addAttribute("pages", IntStream.range(0, pageProducts.getTotalPages()).toArray());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("size", size);
        return "products";
    }

    /**
     * Suppression d'un produit (réservé au rôle ADMIN via Spring Security).
     * On conserve keyword / page pour rester sur la même vue après suppression.
     */
    @GetMapping("/admin/delete")
    public String delete(@RequestParam(name = "id") Long id,
                         @RequestParam(name = "keyword", defaultValue = "") String keyword,
                         @RequestParam(name = "page", defaultValue = "0") int page) {
        productRepository.deleteById(id);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    /**
     * Affiche le formulaire d'ajout d'un nouveau produit (ADMIN).
     */
    @GetMapping("/admin/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }

    /**
     * Enregistre le produit après validation du formulaire.
     */
    @PostMapping("/admin/saveProduct")
    public String saveProduct(@Valid Product product, BindingResult bindingResult,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String keyword,
                              Model model) {
        if (bindingResult.hasErrors()) {
            // On réinjecte page/keyword pour conserver le contexte de la vue d'édition
            model.addAttribute("page", page);
            model.addAttribute("keyword", keyword);
            // Le produit est nouveau (id null) -> vue d'ajout, sinon vue d'édition
            return product.getId() == null ? "new-product" : "edit-product";
        }
        productRepository.save(product);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }

    /**
     * Affiche le formulaire d'édition pré-rempli (ADMIN).
     */
    @GetMapping("/admin/editProduct")
    public String editProduct(Model model,
                              @RequestParam(name = "id") Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String keyword) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));
        model.addAttribute("product", product);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "edit-product";
    }

    /**
     * Redirection de la racine vers la liste des produits.
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/user/index";
    }
}
