package waseem.challenge.orders.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import waseem.challenge.exception.ConflictOrderException;
import waseem.challenge.exception.InvalidOrderStatusException;
import waseem.challenge.exception.OrderHistoryNotFoundException;
import waseem.challenge.exception.OrderNotFoundException;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.OrderRequest;
import waseem.challenge.orders.dto.OrderStatusChangedEvent;
import waseem.challenge.orders.entity.OrderStatus;
import waseem.challenge.orders.entity.Orders;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.mapper.OrderMapper;
import waseem.challenge.orders.repository.OrderRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    public List<OrderDTO> getById(UUID id) {
        return orderRepository.findById(id).stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    public OrderDTO createOrder(OrderRequest request) {
        if (orderRepository.existsById(request.id())) {
            throw new ConflictOrderException(request.id());
        }
        Orders order = new Orders();
        order.setName(request.name());
        order.setId(request.id());

        OrderStatus created = new OrderStatus();
        created.setStatus(Status.CREATED);
        order.addHistory(created);

        Orders saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderStatusChangedEvent(saved, Status.CREATED));

        return orderMapper.toDTO(saved);
    }

    public OrderDTO updateStatus(UUID orderId, Status newStatus) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() ->  new OrderNotFoundException(orderId));

        Status currentStatus = order.getHistory().stream()
                .max(Comparator.comparing(OrderStatus::getTimestamp))
                .map(OrderStatus::getStatus)
                .orElseThrow(() -> new OrderHistoryNotFoundException(orderId));

        if (!isValidTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusException(orderId, currentStatus, newStatus);
        }

        OrderStatus statusEntry = new OrderStatus();
        statusEntry.setStatus(newStatus);
        order.addHistory(statusEntry);

        Orders saved = orderRepository.save(order);

        eventPublisher.publishEvent(new OrderStatusChangedEvent(saved, newStatus));

        return orderMapper.toDTO(saved);
    }

    private boolean isValidTransition(Status current, Status next) {
        return current == Status.CREATED && (next == Status.COMPLETED || next == Status.CANCELLED);
    }


}
