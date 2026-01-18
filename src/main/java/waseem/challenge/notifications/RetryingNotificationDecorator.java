package waseem.challenge.notifications;

import waseem.challenge.orders.entity.Orders;

public class RetryingNotificationDecorator implements NotificationStrategy {

    private final NotificationStrategy delegate;
    private static final int MAX_ATTEMPTS = 3;

    public RetryingNotificationDecorator(NotificationStrategy delegate) {
        this.delegate = delegate;
    }

    @Override
    public void send(Orders order) {
        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            try {
                delegate.send(order);
                return;
            } catch (Exception e) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) throw new RuntimeException("Retry failed", e);
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }
    }

    @Override
    public NotificationType getSupportedType() {
        return delegate.getSupportedType();
    }
}