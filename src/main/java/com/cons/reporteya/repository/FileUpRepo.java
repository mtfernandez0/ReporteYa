package com.cons.reporteya.repository;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.FileUp;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUpRepo extends CrudRepository<FileUp, Long>{

}
