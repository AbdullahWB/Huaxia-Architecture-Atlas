package com.huaxia.atlas.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    long countByStatus(OrderStatus status);

    long countByUserId(Long userId);

    @Query("select coalesce(sum(o.totalPrice), 0) from Order o where o.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") OrderStatus status);
}
