package com.millie.common.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    private long sellerId;

    private long productId;

    private String productName;

    /**
     * 주문 수량
     */
    private int ea;

    /**
     * 판매가
     */
    private int sellPrice;

    /**
     * 수수료율
     */
    private int commissionRate;

}
