package example.microservices.orders.services;

import example.microservices.orders.dto.CreateOrderDTO;
import example.microservices.orders.dto.OrderDTO;
import example.microservices.orders.entities.Order;
import example.microservices.orders.feign.ProductsClient;
import example.microservices.orders.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductsClient productsClient;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductsClient productsClient) {
        this.orderRepository = orderRepository;
        this.productsClient = productsClient;
    }

    public OrderDTO createOrder(CreateOrderDTO orderDTO) {

        ResponseEntity<?> response = productsClient.findProductById(orderDTO.getProductId());

        Order order = mapToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    public List<OrderDTO> findAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> findOrderById(Long id) {
        return orderRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<OrderDTO> updateOrderById(Long id, CreateOrderDTO orderDTO) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            Order existingOrder = optional.get();
            existingOrder.setProductId(orderDTO.getProductId());
            existingOrder.setQuantity(orderDTO.getQuantity());
            existingOrder.setStatus(orderDTO.getStatus());

            Order savedOrder = orderRepository.save(existingOrder);
            return Optional.of(mapToDTO(savedOrder));
        }

        return Optional.empty();
    }

    public boolean deleteOrderById(Long id) {
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            orderRepository.delete(optional.get());
            return true;
        } else {
            return false;
        }
    }

    private Order mapToEntity(CreateOrderDTO orderDTO) {
        return Order.builder()
                .userId(orderDTO.getUserId())
                .productId(orderDTO.getProductId())
                .quantity(orderDTO.getQuantity())
                .status(orderDTO.getStatus())
                .build();
    }

    private OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .status(order.getStatus())
                .build();
    }

}
