package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SellerMinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

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

    @Query(value = """
            SELECT new com.devsuperior.dsmeta.dto.SellerMinDTO(
                sel.name, SUM(s.amount)
            )
            FROM Seller sel
                JOIN sel.sales s
            WHERE s.date BETWEEN :initialDate AND :finalDate
                GROUP BY sel.id, sel.name
            """,
            countQuery = """
                    SELECT COUNT(DISTINCT sel.id)
                    FROM Seller sel
                        JOIN sel.sales s
                    WHERE s.date BETWEEN :initialDate AND :finalDate
                    """)
    List<SellerMinDTO> searchSellersSummary(LocalDate initialDate, LocalDate finalDate);

}
