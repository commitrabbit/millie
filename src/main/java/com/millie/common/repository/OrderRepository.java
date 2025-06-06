package com.millie.common.repository;

import com.millie.common.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findALlBySellerIdAndCreatedDateBetween(
        Long sellerId,
        LocalDate startDate,
        LocalDate endDate
    );
}
