package example.microservices.orders.services;

import example.microservices.orders.dto.OrderDTO;
import example.microservices.orders.entities.Order;
import example.microservices.orders.feign.ProductsClient;
import example.microservices.orders.repositories.OrderRepository;
import feign.FeignException;
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

    public Optional<OrderDTO> createOrder(OrderDTO orderDTO) {
        try{
            System.out.println(orderDTO.getProductId());
            ResponseEntity<?> response = productsClient.findProductById(orderDTO.getProductId());
            if(response.getStatusCode().is2xxSuccessful()) {
                Order order = mapToEntity(orderDTO);
                Order savedOrder = orderRepository.save(order);
                orderDTO = mapToDTO(savedOrder);
                return Optional.of(orderDTO);
            }else {
                return Optional.empty();
            }
        }catch (FeignException.NotFound e){
            return Optional.empty();
        }
    }

    public List<OrderDTO> findAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> findOrderById(Long id) {
        return orderRepository.findById(id).map(this::mapToDTO);
    }

    public Optional<OrderDTO> updateOrderById(Long id, OrderDTO orderDTO) {
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

    private Order mapToEntity(OrderDTO orderDTO) {
        return Order.builder()
                .id(orderDTO.getId())
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
