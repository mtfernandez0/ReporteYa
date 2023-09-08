package com.cons.reporteya.repository;

import com.cons.reporteya.entity.Marker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarkerRepository extends CrudRepository<Marker, Long> {

}
