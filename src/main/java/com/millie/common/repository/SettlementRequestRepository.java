package com.millie.common.repository;

import com.millie.common.domain.entity.SettlementRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementRequestRepository extends JpaRepository<SettlementRequest, Long> {

}
