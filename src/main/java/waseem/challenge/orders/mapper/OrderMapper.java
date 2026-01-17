package waseem.challenge.orders.mapper;

import org.springframework.stereotype.Component;
import waseem.challenge.orders.dto.OrderDTO;
import waseem.challenge.orders.dto.OrderHistoryDTO;
import waseem.challenge.orders.entity.OrderStatus;
import waseem.challenge.orders.entity.Orders;

@Component
public class OrderMapper {

    public OrderDTO toDTO(Orders order) {
        return new OrderDTO(
                order.getId(),
                order.getName(),
                order.getHistory().stream()
                        .map(this::toHistoryDTO)
                        .toList()
        );
    }

    private OrderHistoryDTO toHistoryDTO(OrderStatus status) {
        return new OrderHistoryDTO(
                status.getStatus(),
                status.getTimestamp()
        );
    }
}
