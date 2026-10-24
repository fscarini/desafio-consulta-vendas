package com.devsuperior.dsmeta.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query(value = """
                SELECT s FROM Sale s
                    JOIN FETCH s.seller sel
                WHERE s.date BETWEEN :initialDate AND :finalDate
                    AND UPPER(sel.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))
            """,
            countQuery = """
                    SELECT COUNT(s) FROM Sale s
                        JOIN s.seller sel
                    WHERE s.date BETWEEN :initialDate AND :finalDate
                        AND UPPER(sel.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))
                    """)
    Page<Sale> searchPeriodSales(LocalDate initialDate, LocalDate finalDate, String sellerName, Pageable pageable);

}
