package waseem.challenge.orders.dto;

import java.util.List;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        String name,
        List<OrderHistoryDTO> history
) {}