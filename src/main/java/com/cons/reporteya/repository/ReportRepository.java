package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.Report;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends CrudRepository<Report, Long>,
		PagingAndSortingRepository<Report, Long> {

	List<Report> findAll();

	@Query( "SELECT r FROM Report AS r " +
			"LEFT JOIN Comment AS c ON r.id = c.report.id " +
			"GROUP BY r.id ORDER BY COUNT(c.report.id) DESC, r.created_at DESC LIMIT 3")
	List<Report> findAllHighlightedReports();

	List<Report> findAllByCreatorContactPostcode(String postcode);

	@Query("SELECT r FROM Report AS r ORDER BY r.created_at DESC")
	Page<Report> findAllByOrderByCreationDesc(PageRequest pageRequest);

	@Query(value =
			"SELECT r.* FROM reports AS r " +
			"RIGHT JOIN reports_tags AS rt ON r.id = rt.report_id " +
					"WHERE rt.tags_id = ?1 ORDER BY r.created_at DESC", nativeQuery = true)
	List<Report> findAllByTagsIdOrderByCreationDesc(Long tagId);

	void deleteAllByCreatorId(Long id);
}
