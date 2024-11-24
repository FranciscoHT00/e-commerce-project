package example.microservices.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "products-service", path = "/products")
public interface ProductsClient {

    @GetMapping("/{id}")
    ResponseEntity<?> findProductById(@PathVariable String id);

    @PutMapping("/{id}/reserve/{quantity}")
    ResponseEntity<?> reserveProduct(@PathVariable String id, @PathVariable int quantity);

}
