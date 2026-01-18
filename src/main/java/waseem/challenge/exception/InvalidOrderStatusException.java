package waseem.challenge.exception;

import waseem.challenge.orders.dto.Status;

import java.util.UUID;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException(UUID orderId, Status current, Status next) {
        super("Invalid status transition for order " + orderId + " from " + current + " to " + next);
    }
}
