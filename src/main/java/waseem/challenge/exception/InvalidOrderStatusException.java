package waseem.challenge.exception;

import waseem.challenge.orders.dto.Status;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(Long orderId, Status current, Status next) {
        super("Invalid status transition for order " + orderId + " from " + current + " to " + next);
    }
}
