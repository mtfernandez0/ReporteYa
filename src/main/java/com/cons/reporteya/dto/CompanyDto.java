package com.cons.reporteya.dto;

import com.cons.reporteya.entity.Company;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Builder
public class CompanyDto {

    private String name;

    private String description;

    private String location;

    private String website;

    public static CompanyDto companyToCompanyDto(Company company){
        return CompanyDto.builder()
                .name(company.getName())
                .description(company.getDescription())
                .location(company.getLocation())
                .website(company.getWebsite())
                .build();
    }
}
