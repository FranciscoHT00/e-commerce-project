package example.microservices.orders.feign;

import example.microservices.orders.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "products-service", path = "/products-service")
public interface ProductsClient {

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> findProductById(@PathVariable String id);

}
