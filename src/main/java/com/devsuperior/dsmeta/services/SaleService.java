package com.devsuperior.dsmeta.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SellerMinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repository;

    public SaleMinDTO findById(Long id) {
        Optional<Sale> result = repository.findById(id);
        Sale entity = result.get();
        return new SaleMinDTO(entity);
    }

    public Page<SaleDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
        Page<Sale> result = repository.searchPeriodSales(
                resolveMinDate(minDate, maxDate),
                resolveMaxDate(maxDate),
                name,
                pageable
        );
        return result.map(x -> new SaleDTO(x));
    }

    public List<SellerMinDTO> getSummary(String minDate, String maxDate) {
        return repository.searchSellersSummary(
                resolveMinDate(minDate, maxDate),
                resolveMaxDate(maxDate)
        );
    }

    private LocalDate resolveMinDate(String minDate, String maxDate) {
        if (minDate != null && !minDate.isBlank()){
            return LocalDate.parse(minDate);
        }

        if (maxDate != null && !maxDate.isBlank()) {
            return LocalDate.parse(maxDate).minusYears(1);
        }

        return LocalDate.now().minusYears(1);
    }

    private LocalDate resolveMaxDate(String maxDate) {
        if (maxDate != null && !maxDate.isBlank()) {
            return LocalDate.parse(maxDate);
        }

        return LocalDate.now();
    }



}
