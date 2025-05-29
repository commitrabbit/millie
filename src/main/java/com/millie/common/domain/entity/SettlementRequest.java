package com.millie.common.domain.entity;

import com.millie.common.exception.InvalidSettlementRequestException;
import com.millie.common.type.SettlementRequestStatusType;
import com.millie.seller.dto.SettlementRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class SettlementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 로직 분리를 위해 물리적 연결은 하지 않는다
     */
    private long sellerId;

    private String statusType;

    private LocalDate standardStartDate;

    private LocalDate standardEndDate;

    private LocalDateTime requestDate;

    /**
     * 정산 요청 생성자
     * @param sellerId
     * @param requestDto
     */
    public SettlementRequest(long sellerId, SettlementRequestDto requestDto) {
        this.sellerId = sellerId;
        this.standardStartDate = requestDto.getStandardStartDate();
        this.standardEndDate = requestDto.getStandardEndDate();
        this.statusType = SettlementRequestStatusType.REQUEST.name(); // '요청' 상태
        this.requestDate = LocalDateTime.now();
    }

    /**
     * 정산요청 거절
     */
    public void reject() {
        if (this.statusType != SettlementRequestStatusType.REQUEST.name()) {
            throw new InvalidSettlementRequestException("정산 요청이 '요청' 상태가 아닙니다.");
        }
        this.statusType = SettlementRequestStatusType.REJECT.name();
    }

    /**
     * 정산요청 승인
     */
    public void approve() {
        if (this.statusType != SettlementRequestStatusType.REQUEST.name()) {
            throw new InvalidSettlementRequestException("정산 요청이 '요청' 상태가 아닙니다.");
        }
        this.statusType = SettlementRequestStatusType.APPROVE.name();
    }
}
