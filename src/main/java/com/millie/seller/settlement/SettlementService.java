package com.millie.seller.settlement;

import com.millie.common.exception.BaseException;
import com.millie.common.exception.ErrorCode;
import com.millie.common.repository.SettlementRequestRepository;
import com.millie.seller.dto.SettlementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service( "sellerSettlementService")
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRequestRepository settlementRequestRepository;

    /**
     * 판매자가 정산을 요청하는 메서드
     *
     * @param sellerId 판매자 ID
     * @param requesteDto 정산 요청 정보
     */
    protected void requestSettlement(Long sellerId, SettlementRequestDto requesteDto) {
        try {

        } catch (BaseException bE) {
            throw bE;
        } catch (Exception e) {
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, "정산 요청 중 오류가 발생했습니다.");
        }
    }
}
