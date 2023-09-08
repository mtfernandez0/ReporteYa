package com.cons.reporteya.service;

import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.repository.CompanyRepository;

@Service
public class CompanyService {
	private final CompanyRepository companyRepository;

	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
			}
	
	public Company save(Company company) {
        return companyRepository.save(company);
    }

}
