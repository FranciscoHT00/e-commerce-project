package example.microservices.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users")
public interface UsersClient {

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id);
}
