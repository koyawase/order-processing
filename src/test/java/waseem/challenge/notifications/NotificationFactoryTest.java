package waseem.challenge.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import waseem.challenge.orders.dto.Status;
import waseem.challenge.orders.entity.Orders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationFactoryTest {

    private NotificationFactory factory;
    private NotificationStrategy emailStrategy = mock(NotificationStrategy.class);
    private NotificationStrategy smsStrategy = mock(NotificationStrategy.class);

    @BeforeEach
    void setUp() {
        when(emailStrategy.getSupportedType()).thenReturn(NotificationType.EMAIL);
        when(smsStrategy.getSupportedType()).thenReturn(NotificationType.SMS);

        factory = new NotificationFactory(List.of(emailStrategy, smsStrategy));
    }

    @Test
    void getStrategy_shouldReturnEmailStrategy_whenStatusIsCreated() {
        NotificationStrategy result = factory.getStrategy(Status.CREATED);

        assertNotNull(result);

        Orders order = new Orders();
        result.send(order);

        verify(emailStrategy).send(order);
        verify(smsStrategy, never()).send(any());
    }

    @Test
    void getStrategy_shouldReturnSMSStrategy_whenStatusIsCompleted() {
        NotificationStrategy result = factory.getStrategy(Status.COMPLETED);

        assertNotNull(result);

        Orders order = new Orders();
        result.send(order);

        verify(smsStrategy).send(order);
        verify(emailStrategy, never()).send(any());
    }

    @Test
    void getStrategy_shouldReturnSMSStrategy_whenStatusIsCancelled() {
        NotificationStrategy result = factory.getStrategy(Status.CANCELLED);

        assertNotNull(result);
        Orders order = new Orders();
        result.send(order);

        verify(smsStrategy).send(order);
        verify(emailStrategy, never()).send(any());
    }

}