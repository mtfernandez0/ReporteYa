package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.Report;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long> {

	List<Report> findAll();

//	List<Report> findAllByOrderByCommentsNumber();

	List<Report> findAllByCreatorContactPostcode(String postcode);

	@Query("SELECT r FROM Report AS r ORDER BY r.created_at DESC")
	List<Report> findAllByOrderByCreationDesc();

	@Query(value =
			"SELECT r.* FROM reports AS r " +
			"RIGHT JOIN reports_tags AS rt ON r.id = rt.report_id " +
					"WHERE rt.tags_id = ?1 ORDER BY r.created_at DESC", nativeQuery = true)
	List<Report> findAllByTagsIdOrderByCreationDesc(Long tagId);

	List<Report> findAllByTagsId(Long id);

	boolean existsById(Long id);

	void deleteAllByCreatorId(Long id);
}
