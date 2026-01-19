package waseem.challenge.notifications;

import org.springframework.stereotype.Component;
import waseem.challenge.orders.dto.Status;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class NotificationFactory {

    private final Map<NotificationType, NotificationStrategy> strategyMap;

    public NotificationFactory(List<NotificationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        NotificationStrategy::getSupportedType,
                        strategy -> new RetryingNotificationDecorator(strategy)
                ));
    }

    public NotificationStrategy getStrategy(Status status) {
        NotificationType type = switch (status) {
            case CREATED -> NotificationType.EMAIL;
            case COMPLETED, CANCELLED -> NotificationType.SMS;
            default -> throw new IllegalArgumentException("No notification mapping for status: " + status);
        };

        return Optional.ofNullable(strategyMap.get(type))
                .orElseThrow(() -> new RuntimeException("Strategy not found for type: " + type));
    }
}