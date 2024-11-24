package example.microservices.products.services;

import example.microservices.products.dto.CreateProductDTO;
import example.microservices.products.dto.ProductDTO;
import example.microservices.products.entities.Product;
import example.microservices.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(CreateProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    public List<ProductDTO> findAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public ProductDTO findProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró producto con el ID dado."));
        return mapToDTO(product);
    }

    public ProductDTO updateProductById(String id, CreateProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró producto con el ID dado."));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setStock(productDTO.getStock());

        Product savedProduct = productRepository.save(existingProduct);
        return mapToDTO(savedProduct);

    }

    public ProductDTO reserveProduct(String id, int quantity) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró producto con el ID dado."));

        if(existingProduct.getStock() < quantity) {
            throw new IllegalArgumentException("No hay suficiente stock del producto indicado.");
        }else {
            existingProduct.setStock(existingProduct.getStock() - quantity);
            existingProduct.setReservedStock(existingProduct.getReservedStock() + quantity);
            Product savedProduct = productRepository.save(existingProduct);
            return mapToDTO(savedProduct);
        }
    }


    public void deleteProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró orden con el ID dado."));

        productRepository.delete(product);
    }

    private Product mapToEntity(CreateProductDTO productDTO) {
        return Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .reservedStock(productDTO.getReservedStock())
                .build();
    }

    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .reservedStock(product.getReservedStock())
                .build();
    }

}
