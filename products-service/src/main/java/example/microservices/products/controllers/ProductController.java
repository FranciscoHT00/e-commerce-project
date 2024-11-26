package example.microservices.products.controllers;

import example.microservices.products.dto.CreateProductDTO;
import example.microservices.products.dto.ProductDTO;
import example.microservices.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> findAllProducts(){
        List<ProductDTO> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findProductById(@PathVariable String id) {

        try {
            ProductDTO product = productService.findProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody CreateProductDTO productDTO, @PathVariable String id) {
        try {
            ProductDTO updatedProduct = productService.updateProductById(id, productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}/reserve/{quantity}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reserveProduct(@PathVariable String id, @PathVariable int quantity) {
        try {
            ProductDTO updatedProduct = productService.reserveProduct(id, quantity);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {

        try {
            productService.deleteProductById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Producto eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping("/adminTest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> adminTest(){
        return ResponseEntity.status(HttpStatus.OK).body("Hola Admin.");
    }
}
