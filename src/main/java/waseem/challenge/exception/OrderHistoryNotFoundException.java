package waseem.challenge.exception;

public class OrderHistoryNotFoundException extends RuntimeException {
    public OrderHistoryNotFoundException(Long orderId) {
        super("Order history not found for order id: " + orderId);
    }
}
