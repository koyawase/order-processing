package waseem.challenge.orders;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import waseem.challenge.exception.ConflictOrderException;
import waseem.challenge.exception.InvalidOrderStatusException;
import waseem.challenge.exception.OrderHistoryNotFoundException;
import waseem.challenge.exception.OrderNotFoundException;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.OrderRequest;
import waseem.challenge.orders.dto.OrderStatusChangedEvent;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.entity.OrderStatus;
import waseem.challenge.orders.entity.Orders;
import waseem.challenge.orders.mapper.OrderMapper;
import waseem.challenge.orders.repository.OrderRepository;
import waseem.challenge.orders.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher  eventPublisher;

    // Use actual implementation since its a simple entity to dto converter
    @Spy
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Nested
    class GetAllOrdersTests {
        @Test
        void getAll_shouldReturnListOfOrderDTOs() {
            UUID id1 = UUID.randomUUID();
            UUID id2 = UUID.randomUUID();

            Orders order1 = createTestOrder(id1, "Laptop", Status.CREATED);
            Orders order2 = createTestOrder(id2, "Mouse", Status.CREATED);

            List<Orders> entities = List.of(order1, order2);
            when(orderRepository.findAll()).thenReturn(entities);

            List<OrderDTO> result = orderService.getAll();

            assertEquals(2, result.size());

            assertEquals("Laptop", result.get(0).name());
            assertEquals("Mouse", result.get(1).name());
        }

        @Test
        void getAll_whenNoOrdersExist_shouldReturnEmptyList() {
            when(orderRepository.findAll()).thenReturn(List.of());

            List<OrderDTO> result = orderService.getAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(orderRepository).findAll();
        }
    }

    @Nested
    class GetOrderByIdTests {
        @Test
        void getById_shouldReturnMappedOrder() {
            UUID id = UUID.randomUUID();
            Orders order = createTestOrder(id, "Laptop", Status.CREATED);
            when(orderRepository.findById(id)).thenReturn(Optional.of(order));

            OrderDTO result = orderService.getById(id);

            assertEquals(id, result.id());
            assertEquals("Laptop", result.name());
            assertEquals(1, result.history().size());
            assertEquals(Status.CREATED, result.history().get(0).status());
        }

        @Test
        void getById_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
            UUID id = UUID.randomUUID();
            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () ->
                    orderService.getById(id)
            );

            assertTrue(exception.getMessage().contains(id.toString()));
            verifyNoInteractions(orderMapper);
        }
    }

    @Nested
    class CreateOrderTests {
        @Test
        void createOrder_shouldSaveAndReturnDto_whenIdIsUnique() {
            UUID id = UUID.randomUUID();
            OrderRequest request = new OrderRequest("New Order", id);

            when(orderRepository.existsById(id)).thenReturn(false);

            Orders savedEntity = createTestOrder(id, "New Order", Status.CREATED);
            when(orderRepository.save(any(Orders.class))).thenReturn(savedEntity);

            OrderDTO result = orderService.createOrder(request);

            assertEquals("New Order", result.name());

            verify(orderRepository).existsById(id);
            verify(orderRepository).save(any(Orders.class));

            verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
        }

        @Test
        void createOrder_shouldThrowConflictException_whenIdAlreadyExists() {
            UUID existingId = UUID.randomUUID();
            OrderRequest request = new OrderRequest("Duplicate Order", existingId);

            when(orderRepository.existsById(existingId)).thenReturn(true);

            assertThrows(ConflictOrderException.class, () ->
                    orderService.createOrder(request)
            );

            verify(orderRepository, never()).save(any());
            verifyNoInteractions(eventPublisher);
        }
    }

    @Nested
    class UpdateStatusTests {

        @Test
        void updateStatus_shouldSucceed_SimpleVersion() {
            UUID id = UUID.randomUUID();
            Orders existingOrder = createTestOrder(id, "Laptop", Status.CREATED);

            when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
            when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

            OrderDTO result = orderService.updateStatus(id, Status.COMPLETED);

            assertEquals(2, existingOrder.getHistory().size());
            verify(orderRepository).save(existingOrder);
            verify(eventPublisher).publishEvent(any(OrderStatusChangedEvent.class));
        }

        @Test
        void updateStatus_shouldThrowOrderNotFoundException_whenOrderDoesNotExist() {
            UUID id = UUID.randomUUID();

            when(orderRepository.findById(id)).thenReturn(Optional.empty());

            OrderNotFoundException exception = assertThrows(OrderNotFoundException.class, () ->
                    orderService.updateStatus(id, Status.CREATED)
            );

            assertTrue(exception.getMessage().contains(id.toString()));

            verify(orderRepository, never()).save(any());
            verifyNoInteractions(eventPublisher);
            verifyNoInteractions(orderMapper);
        }

        @Test
        void updateStatus_shouldThrowOrderHistoryNotFound_whenHistoryIsEmpty() {
            UUID id = UUID.randomUUID();
            Orders orderWithNoHistory = createTestOrderNoHistory(id, "Broken Order", Status.CREATED);

            when(orderRepository.findById(id)).thenReturn(Optional.of(orderWithNoHistory));

            OrderHistoryNotFoundException exception = assertThrows(OrderHistoryNotFoundException.class, () ->
                    orderService.updateStatus(id, Status.CREATED)
            );


            assertTrue(exception.getMessage().contains(id.toString()));

            verify(orderRepository, never()).save(any());
            verifyNoInteractions(eventPublisher);
        }

        @Test
        void updateStatus_shouldThrowInvalidOrderStatusException_whenTransitionIsIllegal() {
            UUID id = UUID.randomUUID();
            Orders existingOrder = createTestOrder(id, "laptop", Status.COMPLETED);

            when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));

            InvalidOrderStatusException exception = assertThrows(InvalidOrderStatusException.class, () ->
                    orderService.updateStatus(id, Status.COMPLETED)
            );

            assertTrue(exception.getMessage().contains(id.toString()));

            verify(orderRepository, never()).save(any());
            verifyNoInteractions(eventPublisher);
        }
    }

    private Orders createTestOrder(UUID id, String name, Status currentStatus) {
        Orders order = new Orders();
        order.setId(id);
        order.setName(name);

        OrderStatus history = new OrderStatus();
        history.setStatus(currentStatus);
        history.setTimestamp(LocalDateTime.now());

        order.addHistory(history);
        return order;
    }

    private Orders createTestOrderNoHistory(UUID id, String name, Status currentStatus) {
        Orders order = new Orders();
        order.setId(id);
        order.setName(name);
        return order;
    }
}
