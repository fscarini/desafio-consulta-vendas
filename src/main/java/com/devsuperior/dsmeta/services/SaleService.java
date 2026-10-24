package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleDTO;
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


	public Page<SaleDTO> getReport(String initialDate, String finalDate, String name, Pageable pageable){
		Page<Sale> result = repository.searchPeriodSales(
				resolveInitialDate(initialDate),
				resolveFinalDate(finalDate),
				name,
				pageable
		);
		return result.map(x -> new SaleDTO(x));
	}

	private LocalDate resolveInitialDate(String initialDate){
		if (initialDate == null || initialDate.trim().isEmpty()) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()).minusYears(1);
		}
		return LocalDate.parse(initialDate);
	}

	private LocalDate resolveFinalDate(String finalDate){
		if (finalDate == null || finalDate.trim().isEmpty()) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}
		return LocalDate.parse(finalDate);
	}

}
