package example.microservices.products.services;

import example.microservices.products.dto.ProductDTO;
import example.microservices.products.entities.Product;
import example.microservices.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        productDTO = mapToDTO(savedProduct);
        return productDTO;
    }

    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public Optional<ProductDTO> findProductById(String id) {
        return productRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<ProductDTO> updateProductById(String id, ProductDTO productDTO) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            Product existingProduct = optional.get();
            existingProduct.setName(productDTO.getName());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setStock(productDTO.getStock());

            Product savedProduct = productRepository.save(existingProduct);
            return Optional.of(mapToDTO(savedProduct));
        }

        return Optional.empty();
    }

    public boolean deleteProductById(String id) {
        Optional<Product> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            productRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    public void deleteById(String id){
        productRepository.deleteById(id);
    }

    private Product mapToEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .build();
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

}
