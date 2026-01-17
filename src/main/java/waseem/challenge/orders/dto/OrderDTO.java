package waseem.challenge.orders.dto;

import java.util.List;

public record OrderDTO(
        Long id,
        String name,
        List<OrderHistoryDTO> history
) {}