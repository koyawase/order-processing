package waseem.challenge.exception;

import java.util.UUID;

public class ConflictOrderException extends RuntimeException {
    public ConflictOrderException(UUID orderId) {
        super("Order with order id " + orderId + " already exists.");
    }
}
