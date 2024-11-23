package example.microservices.products.controllers;

import example.microservices.products.dto.CreateProductDTO;
import example.microservices.products.dto.ProductDTO;
import example.microservices.products.entities.Product;
import example.microservices.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody CreateProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> findAllProducts(){
        List<ProductDTO> products = productService.findAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findProductById(@PathVariable String id) {
        Optional<ProductDTO> productDTO = productService.findProductById(id);
        if (productDTO.isPresent()) {
            return ResponseEntity.ok(productDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un producto con la ID dada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody CreateProductDTO productDTO, @PathVariable String id) {
        Optional<ProductDTO> updatedProduct = productService.updateProductById(id, productDTO);

        if (updatedProduct.isPresent()) {
            return ResponseEntity.ok(updatedProduct.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró un producto con la ID dada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (productService.deleteProductById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
