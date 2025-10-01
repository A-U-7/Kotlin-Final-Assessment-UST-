package techieamit_it.service;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import techieamit_it.entity.Order;
import techieamit_it.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetAllOrders")
    public List<Order> getAllOrders() {
        kafkaTemplate.send("order-events", "Fetching all orders");
        return orderRepository.findAll();
    }
    
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackGetOrderById")
    public Optional<Order> getOrderById(Long id) {
        kafkaTemplate.send("order-events", "Fetching order with id: " + id);
        return orderRepository.findById(id);
    }
    
    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackCreateOrder")
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send("order-events", "Created order: " + savedOrder.getProductName());
        return savedOrder;
    }
    
    public Order updateOrder(Long id, Order order) {
        order.setId(id);
        Order updatedOrder = orderRepository.save(order);
        kafkaTemplate.send("order-events", "Updated order: " + updatedOrder.getProductName());
        return updatedOrder;
    }
    
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        kafkaTemplate.send("order-events", "Deleted order with id: " + id);
    }
    
    // Fallback methods
    public List<Order> fallbackGetAllOrders(Throwable t) {
        kafkaTemplate.send("order-events", "Fallback: Error fetching orders");
        return List.of();
    }
    
    public Optional<Order> fallbackGetOrderById(Long id, Throwable t) {
        kafkaTemplate.send("order-events", "Fallback: Error fetching order with id: " + id);
        return Optional.empty();
    }
    
    public Order fallbackCreateOrder(Order order, Throwable t) {
        kafkaTemplate.send("order-events", "Fallback: Error creating order");
        return new Order("Fallback Product", java.math.BigDecimal.ZERO, "PENDING");
    }
}