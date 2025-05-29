package com.millie.seller.settlement;

import com.millie.common.model.ResponseModel;
import com.millie.seller.dto.SettlementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 판매자(Seller) 정산 담당 컨트롤러
 */
@RestController("sellerSettlementController")
@RequiredArgsConstructor
public class SettlementController {

    @Qualifier("sellerSettlementService")
    private final SettlementService settlementService;

    /**
     * 정산요청
     */
    @PostMapping("/seller/settlement")
    public ResponseModel<Void> requestSettlement(@RequestBody SettlementRequestDto requestDto) {
        Long sellerId = 1L; // 예시로 하드코딩된 판매자 ID, 실제로는 인증된 사용자 ID를 사용해야 함
        settlementService.requestSettlement(sellerId, requestDto);
        return ResponseModel.success();
    }
}
