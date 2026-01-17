package waseem.challenge.notifications;

import org.springframework.stereotype.Component;

@Component
public class SmsNotificationStrategy implements NotificationStrategy {
    @Override
    public void send(waseem.challenge.orders.entity.Orders order) {
        System.out.println("Sending SMS notification for Order ID: " + order.getId());
    }

    @Override
    public NotificationType getSupportedType() {
        return NotificationType.SMS;
    }
}
