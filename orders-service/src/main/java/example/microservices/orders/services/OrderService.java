package example.microservices.orders.services;

import example.microservices.orders.dto.CreateOrderDTO;
import example.microservices.orders.dto.OrderDTO;
import example.microservices.orders.entities.Order;
import example.microservices.orders.feign.ProductsClient;
import example.microservices.orders.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        productsClient.reserveProduct(orderDTO.getProductId(), orderDTO.getQuantity());

        Order order = mapToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    public List<OrderDTO> findAllOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró orden con el ID dado."));
        return mapToDTO(order);
    }

    public OrderDTO updateOrderById(Long id, CreateOrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró orden con el ID dado."));

        if (!existingOrder.getProductId().equals(orderDTO.getProductId())) {
            productsClient.reserveProduct(existingOrder.getProductId(), -1*existingOrder.getQuantity());
            productsClient.reserveProduct(orderDTO.getProductId(), orderDTO.getQuantity());
        } else {
            int quantityDifference = orderDTO.getQuantity() - existingOrder.getQuantity();
            productsClient.reserveProduct(orderDTO.getProductId(), quantityDifference);
        }

        existingOrder.setUserId(orderDTO.getUserId());
        existingOrder.setProductId(orderDTO.getProductId());
        existingOrder.setQuantity(orderDTO.getQuantity());
        existingOrder.setStatus(orderDTO.getStatus());

        Order savedOrder = orderRepository.save(existingOrder);
        return mapToDTO(savedOrder);
    }

    public OrderDTO changeStatus(Long id, String status) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró orden con el ID dado."));

        existingOrder.setStatus(status);

        Order savedOrder = orderRepository.save(existingOrder);
        return mapToDTO(savedOrder);
    }

    public void deleteOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró orden con el ID dado."));

        orderRepository.delete(order);
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
