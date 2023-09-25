package com.cons.reporteya.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cons.reporteya.entity.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long>{
	List<Company> findAll();

	boolean existsByName(String name);
}
