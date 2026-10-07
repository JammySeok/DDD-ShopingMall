package jammy.dddshopmall.order.domain;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findById(OrderNo number);
    void save(Order order);
}