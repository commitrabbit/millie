package com.millie.admin.settlement;

import com.millie.common.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 어드민 정산 담당 컨트롤러
 */
@RestController("adminSettlementController")
@RequiredArgsConstructor
public class SettlementController {

    @Qualifier("adminSettlementService")
    private final SettlementService settlementService;

    /**
     * 정산요청목록 조회
     */
    @GetMapping("/admin/settlement-requests")
    public void getSettlementRequestList() {

    }

    /**
     * 정산요청 거절
     */
    @PutMapping("/admin/settlement-requests/{requestId}/rejection")
    public ResponseModel<Void> rejectSettlementRequest(@PathVariable Long requestId) {
        settlementService.rejectSettlementRequest(requestId);
        return ResponseModel.success();
    }

    /**
     * 정산요청 승인
     */
    @PutMapping("/admin/settlement-requests/{requestId}/approval")
    public ResponseModel<Void> approveSettlementRequest(@PathVariable Long requestId) {
        settlementService.approveSettlementRequest(requestId);
        return ResponseModel.success();
    }
}
