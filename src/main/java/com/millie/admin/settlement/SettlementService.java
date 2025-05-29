package com.millie.admin.settlement;

import com.millie.common.domain.entity.Order;
import com.millie.common.domain.entity.OrderItem;
import com.millie.common.domain.entity.SettlementRequest;
import com.millie.common.exception.BaseException;
import com.millie.common.exception.ErrorCode;
import com.millie.common.repository.OrderRepository;
import com.millie.common.repository.SettlementRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

@Service("adminSettlementService")
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRequestRepository settlementRequestRepository;

    private final OrderRepository orderRepository;

    @Transactional
    public void rejectSettlementRequest(Long requestId) {
        try {
            SettlementRequest settlementRequest = getSettlementRequest(requestId);
            settlementRequest.reject();
            settlementRequestRepository.save(settlementRequest);
        } catch (BaseException bE) {
            throw bE;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Transactional
    public void approveSettlementRequest(Long requestId) {
        try {
            SettlementRequest settlementRequest = getSettlementRequest(requestId);
            settlementRequest.approve();
            settlementRequestRepository.save(settlementRequest);
        } catch (BaseException bE) {
            throw bE;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private SettlementRequest getSettlementRequest(Long requestId) {
        return settlementRequestRepository.findById(requestId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_SETTLEMENT_REQUEST);
    }

    private void calculate(Long sellerId, LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderRepository.findALlBySellerIdAndCreatedDateBetween(sellerId, startDate, endDate);
        if (CollectionUtils.isEmpty(orders)) {
            throw new BaseException(ErrorCode.NOT_FOUND_ORDER);
        }
        // 판매자에게 귀속되는 주문상품 추출
        List<OrderItem> selectedOrderItems = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> orderItem.getSellerId() == sellerId)
                .toList();

        if (CollectionUtils.isEmpty(selectedOrderItems)) {
            throw new BaseException(ErrorCode.NOT_FOUND_ORDER);
        }

    }

    private long extractSellerAmount(List<OrderItem> orderItems) {
        int totalSales = 0;
        int totalCommission = 0;

        for (OrderItem item : orderItems) {
            int itemSales = item.getSellPrice() * item.getEa(); // 총 판매 금액 합산
            int commission = (itemSales * item.getCommissionRate()) / 100;

            totalSales += itemSales;
            totalCommission += commission;
        }

        return totalSales - totalCommission;
    }
}
