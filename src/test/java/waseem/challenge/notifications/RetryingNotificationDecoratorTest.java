package waseem.challenge.notifications;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import waseem.challenge.orders.entity.Orders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RetryingNotificationDecoratorTest {

    @Mock
    private NotificationStrategy strategy;

    private RetryingNotificationDecorator decorator;

    @BeforeEach
    void setUp() {
        decorator = new RetryingNotificationDecorator(strategy);
    }

    @Test
    void shouldRetryOnFailure() {
        Orders order = new Orders();

        doThrow(new RuntimeException("Temporary Network Failure")) // 1st attempt error
                .doNothing()                                       // 2nd attempt success
                .when(strategy).send(order);

        decorator.send(order);

        verify(strategy, times(2)).send(order);
    }

    @Test
    void shouldPropagateException_whenAllRetriesFail() {
        Orders order = new Orders();

        doThrow(new RuntimeException("Permanent Failure")).when(strategy).send(order);

        assertThrows(RuntimeException.class, () -> decorator.send(order));

        verify(strategy, times(3)).send(order);
    }
}
