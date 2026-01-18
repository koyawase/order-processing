package waseem.challenge.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import waseem.challenge.orders.entity.Orders;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
}
