package waseem.challenge.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import waseem.challenge.orders.entity.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
