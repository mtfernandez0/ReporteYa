package com.cons.reporteya.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.FileUp;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUpRepo extends CrudRepository<FileUp, Long>{

    @Modifying
    @Query("DELETE FROM FileUp AS f WHERE f.reporte.id = ?1")
    void deleteAllByReporteId(Long report_id);

}
