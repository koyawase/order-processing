package waseem.challenge.notifications;

import waseem.challenge.orders.entity.Orders;

public interface NotificationStrategy {
    void send(Orders order);
    NotificationType getSupportedType();
}
