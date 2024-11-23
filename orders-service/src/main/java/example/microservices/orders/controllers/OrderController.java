package example.microservices.orders.controllers;

import example.microservices.orders.dto.CreateOrderDTO;
import example.microservices.orders.dto.OrderDTO;
import example.microservices.orders.services.OrderService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderDTO orderDTO) {

        try {
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<OrderDTO>> findAllOrders(){
        List<OrderDTO> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable Long id) {
        Optional<OrderDTO> orderDTO = orderService.findOrderById(id);

        if (orderDTO.isPresent()) return ResponseEntity.ok(orderDTO.get());
        else return ResponseEntity.ok("Order not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody CreateOrderDTO orderDTO, @PathVariable Long id) {
        Optional<OrderDTO> updatedOrder = orderService.updateOrderById(id, orderDTO);
        return updatedOrder.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderService.deleteOrderById(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
