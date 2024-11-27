package example.microservices.orders.controllers;

import example.microservices.orders.dto.CreateOrderDTO;
import example.microservices.orders.dto.OrderDTO;
import example.microservices.orders.services.OrderService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OrderDTO>> findAllOrders(){
        List<OrderDTO> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.findOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body(order);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody CreateOrderDTO orderDTO, @PathVariable Long id) {

        try {
            OrderDTO updatedOrder = orderService.updateOrderById(id, orderDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (FeignException.NotFound|IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @PatchMapping("/{id}/change-status/{status}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @PathVariable String status) {

        try {
            OrderDTO updatedOrder = orderService.changeStatus(id, status);
            return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
        } catch (FeignException.NotFound|IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Orden eliminada correctamente.");
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
