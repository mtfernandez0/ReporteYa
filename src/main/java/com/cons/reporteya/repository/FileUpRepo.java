package com.cons.reporteya.repository;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.FileUp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUpRepo extends CrudRepository<FileUp, Long>{

    void deleteAllByReporteId(Long report_id);

}
