package com.cons.reporteya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Comment;
import com.cons.reporteya.repository.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	public void save(Comment newComment) {
		commentRepository.save(newComment);
	}
}
