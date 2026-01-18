package waseem.challenge.exception;

import java.util.UUID;

public class OrderHistoryNotFoundException extends RuntimeException {
    public OrderHistoryNotFoundException(UUID orderId) {
        super("Order history not found for order id: " + orderId);
    }
}
