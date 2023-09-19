package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

	List<Report> findAll();

	List<Report> findAllByCreatorContactPostcode(String postcode);

	List<Report> findAllByTagsId(Long id);

	boolean existsById(Long id);

	void deleteAllByCreatorId(Long id);
}
