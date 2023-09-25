package com.cons.reporteya.validator;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.repository.CompanyRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CompanyValidator implements Validator {

    private final CompanyRepository companyRepository;

    public CompanyValidator(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Company.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Company company = (Company) target;

        if (companyRepository.existsByName(company.getName()))
            errors.rejectValue(
                    "name",
                    "Match");

    }
}
