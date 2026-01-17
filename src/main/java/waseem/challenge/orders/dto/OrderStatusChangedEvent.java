package waseem.challenge.orders.dto;

import waseem.challenge.orders.entity.Orders;

public record OrderStatusChangedEvent(Orders order, Status newStatus) {}