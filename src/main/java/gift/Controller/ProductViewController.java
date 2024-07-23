package gift.Controller;

import gift.DTO.ProductDTO;
import gift.Entity.ProductEntity;
import gift.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductViewController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/view")
    public String viewAllProducts(Pageable pageable, Model model) {
        Page<ProductDTO> productPage = productService.getProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "product-list";
    }

    @GetMapping("/products/view/{id}")
    public String viewProductById(@PathVariable Long id, Model model) {
        productService.findProductById(id).ifPresent(product -> model.addAttribute("product", product));
        return "product-detail";
    }

    @PostMapping("/products")
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity productEntity) {
        ProductEntity savedProduct = productService.saveProduct(productEntity);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable Long id, @RequestBody ProductEntity productEntity) {
        ProductEntity updatedProduct = productService.updateProduct(id, productEntity);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
