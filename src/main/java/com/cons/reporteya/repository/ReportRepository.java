package com.cons.reporteya.repository;

import com.cons.reporteya.entity.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

    List<Report> findAll();
}
