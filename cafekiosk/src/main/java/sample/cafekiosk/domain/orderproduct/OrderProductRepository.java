package sample.cafekiosk.domain.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;
import sample.cafekiosk.domain.order.Order;

public interface OrderProductRepository extends JpaRepository<Order, Long> {
}
