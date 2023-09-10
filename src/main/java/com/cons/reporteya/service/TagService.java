package com.cons.reporteya.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Tag;
import com.cons.reporteya.repository.ReportRepository;
import com.cons.reporteya.repository.TagRepository;

@Service
public class TagService {
	private final TagRepository tagRepository;

	public TagService(ReportRepository reportRepository, TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public Tag createTag(String subject) {
		Tag newTag = Tag.builder().subject(subject).build();
		return tagRepository.save(newTag);
	}

	public Iterable<Tag> saveAll(List<Tag> tags) {
		return tagRepository.saveAll(tags);
	}

	public List<Tag> findBySubject(String subject) {
		return tagRepository.findAllBySubject(subject);
	}

	public void saveTag(Tag tag) {
		tagRepository.save(tag);
	}

}
