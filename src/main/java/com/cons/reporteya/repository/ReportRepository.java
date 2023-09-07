package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.Report;

public interface ReportRepository extends CrudRepository<Report, Long> {

	List<Report> findAll();

	boolean existsById(Long id);

}
