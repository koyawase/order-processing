package waseem.challenge.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import waseem.challenge.orders.controller.OrderController;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.OrderRequest;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.service.OrderService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void getAll_shouldReturnOkStatusAndList() {
        List<OrderDTO> expectedList = List.of(
                new OrderDTO(UUID.randomUUID(), "Laptop", List.of()),
                new OrderDTO(UUID.randomUUID(), "Mouse", List.of())
        );
        when(orderService.getAll()).thenReturn(expectedList);

        ResponseEntity<List<OrderDTO>> response = orderController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(orderService, times(1)).getAll();
    }

    @Test
    void getById_shouldReturnOkStatusAndSingleOrder() {
        UUID id = UUID.randomUUID();
        OrderDTO expected = new OrderDTO(id, "Laptop", List.of());
        when(orderService.getById(id)).thenReturn(expected);

        ResponseEntity<OrderDTO> response = orderController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().id());
        verify(orderService, times(1)).getById(id);
    }

    @Test
    void updateStatus_shouldReturnOkStatusAndUpdatedOrder() {
        UUID id = UUID.randomUUID();
        Status newStatus = Status.COMPLETED;
        OrderDTO updatedOrder = new OrderDTO(id, "Laptop", List.of());

        when(orderService.updateStatus(id, newStatus)).thenReturn(updatedOrder);

        ResponseEntity<OrderDTO> response = orderController.updateStatus(id, newStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(orderService, times(1)).updateStatus(id, newStatus);
    }

    @Test
    void create_shouldReturnCreatedStatus() {
        UUID id = UUID.randomUUID();
        OrderRequest request = new OrderRequest("Laptop", id);
        OrderDTO expected = new OrderDTO(id, "Laptop", List.of());
        when(orderService.createOrder(any())).thenReturn(expected);

        ResponseEntity<OrderDTO> response = orderController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.name(), response.getBody().name());
        assertEquals(request.id(), response.getBody().id());
    }

}