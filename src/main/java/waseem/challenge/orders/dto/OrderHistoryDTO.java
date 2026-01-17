package waseem.challenge.orders.dto;

import java.time.LocalDateTime;

public record OrderHistoryDTO(
        Status status,
        LocalDateTime timestamp
) {}