package basic.shop.order.command.domain.repository;

import basic.shop.order.command.domain.entiy.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
