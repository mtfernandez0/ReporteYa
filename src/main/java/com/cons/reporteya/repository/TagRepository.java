package com.cons.reporteya.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cons.reporteya.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

	List<Tag> findAll();

	Tag findBySubject(String subject);

	Tag save(String subject);

	List<Tag> findAllBySubject(String subject);

}
