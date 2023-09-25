package com.cons.reporteya.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.entity.Tag;
import com.cons.reporteya.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final TagService tagService;
	private static final int PAGE_SIZE = 3;

	public ReportService(ReportRepository reportRepository, TagService tagService) {
		this.reportRepository = reportRepository;
		this.tagService = tagService;
	}

	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	public List<Report> findAllByCreatorContactPostcode(String postcode){
		return reportRepository.findAllByCreatorContactPostcode(postcode);
	}

	public List<Report> findAllHighlightedReports(){
		return reportRepository.findAllHighlightedReports();
	}

	public Page<Report> findAllByTagsIdOrderByCreationDesc(Long tagId, int pageNumber){
		PageRequest pageRequest = PageRequest.of(
				pageNumber,
				PAGE_SIZE);
		return reportRepository.findAllByTagsIdOrderByCreationDesc(tagId, pageRequest);
	}

	public Report createReport(Report report, List<String> subjects) {
		List<Tag> tags = generateTagList(subjects);

		tags.forEach(tag -> {
			if (tag.getId() == null) {
				tagService.saveTag(tag);
			}
		});

		report.setTags(tags);
		return reportRepository.save(report);
	}

	private List<Tag> generateTagList(List<String> subjects) {
		List<Tag> tags = new ArrayList<>();

		subjects.forEach(subject -> {
			List<Tag> existingTags = tagService.findBySubject(subject);

			if (existingTags.isEmpty()) {
				Tag newTag = Tag.builder().subject(subject).reports(new ArrayList<>()).build();
				tags.add(newTag);
			} else {
				tags.addAll(existingTags);
			}
		});

		return tags;
	}

	public Optional<Report> findById(Long aLong) {
		return reportRepository.findById(aLong);
	}

	public Report updateReport(Report existingReport, Report updatedReport) {
		if (existingReport != null && updatedReport != null) {

			existingReport.setTitle(updatedReport.getTitle());
			existingReport.setDescription(updatedReport.getDescription());
			return reportRepository.save(existingReport);
		} else {
			throw new IllegalArgumentException("El reporte no puede ser nulo.");
		}
	}

	@Transactional
	public void deleteAllByCreatorId(Long id){
		reportRepository.deleteAllByCreatorId(id);
	}

	public void deleteReport(Long id) {
		if (reportRepository.existsById(id)) {
			reportRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("No se encontró el reporte a eliminar.");
		}
	}

	public Page<Report> reportsPerPage(int pageNumber){
		PageRequest pageRequest = PageRequest.of(
				pageNumber,
				PAGE_SIZE);

		return reportRepository.findAllByOrderByCreationDesc(pageRequest);
	}
}
