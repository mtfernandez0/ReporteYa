package com.cons.reporteya.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cons.reporteya.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
