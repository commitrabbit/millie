package com.millie.seller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.millie.common.domain.entity.SettlementRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class SettlementRequestDto {

    /**
     * 정산 시작일
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate standardStartDate;

    /**
     * 정산 종료일
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate standardEndDate;

}
