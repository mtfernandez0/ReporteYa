package com.cons.reporteya.service;

import com.cons.reporteya.validator.CompanyValidator;
import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.repository.CompanyRepository;
import org.springframework.validation.Errors;

import java.util.List;

@Service
public class CompanyService {
	private final CompanyRepository companyRepository;
	private final CompanyValidator companyValidator;

	public CompanyService(CompanyRepository companyRepository,
						  CompanyValidator companyValidator) {
		this.companyRepository = companyRepository;
		this.companyValidator = companyValidator;
	}

	public Company save(Company company) {
        return companyRepository.save(company);
    }

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public void validateCompany(Company company, Errors errors){
		companyValidator.validate(company, errors);
	}
}
