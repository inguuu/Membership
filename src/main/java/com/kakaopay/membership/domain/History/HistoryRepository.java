package com.kakaopay.membership.domain.History;

import com.kakaopay.membership.domain.History.History;
import com.kakaopay.membership.dto.ResponseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,String> {
    History save(History history);
    @Query(value =
            "SELECT approved_at AS approvedAt,type,category,partner_name AS partnerName " +
            "FROM tb_history " +
            "WHERE barcode = ?1 AND approved_at >= ?2 AND approved_at <= ?3",
            nativeQuery = true)
    List<HistoryResponseInterface> findByBarCodeAndStartDateAndEndDate(String barcode, String startDate, String endDate);

}
