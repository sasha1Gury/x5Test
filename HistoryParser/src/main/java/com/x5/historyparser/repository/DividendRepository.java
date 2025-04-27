package com.x5.historyparser.repository;

import com.x5.historyparser.entity.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {
    @Query("SELECT d FROM Dividend d WHERE d.companyCode = '" + Dividend.COMPANY_CODE_X5 + "' AND d.state = '" + Dividend.STATE_PAID + "'")
    List<Dividend> findPaidDividendsForX5();

    boolean existsByCompanyCodeAndCloseRegisterDate(String companyCode, LocalDate closeRegisterDate);

}