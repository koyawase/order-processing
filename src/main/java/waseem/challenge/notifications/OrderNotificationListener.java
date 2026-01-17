package waseem.challenge.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import waseem.challenge.orders.dto.OrderStatusChangedEvent;

@Component
public class OrderNotificationListener {

    @Autowired
    NotificationFactory notificationFactory;

    @EventListener
    public void handleOrderStatusChange(OrderStatusChangedEvent event) {
        NotificationStrategy strategy = notificationFactory.getStrategy(event.newStatus());
        strategy.send(event.order());
    }
}