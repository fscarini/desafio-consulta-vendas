package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.entities.Sale;

import java.time.LocalDate;

public record SaleDTO(
        Long id,
        LocalDate date,
        Double amount,
        String seller
) {

    public SaleDTO(Sale entity){
        this(
                entity.getId(),
                entity.getDate(),
                entity.getAmount(),
                entity.getSeller().getName()
        );
    }

}
