package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cons.reporteya.entity.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	List<Tag> findAll();

	Tag findBySubject(String subject);

	Tag save(String subject);

	List<Tag> findAllBySubject(String subject);


	@Query(value = "SELECT t.* FROM tags AS t RIGHT JOIN reports_tags AS rt ON rt.tags_id = t.id GROUP BY t.id ORDER BY COUNT(t.id) DESC", nativeQuery = true)
	List<Tag> findAllOrderBySubjectCount();

}
