package com.cons.reporteya.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cons.reporteya.entity.Marker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.entity.Tag;
import com.cons.reporteya.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;
	private final TagService tagService;

	public ReportService(ReportRepository reportRepository, TagService tagService) {
		this.reportRepository = reportRepository;
		this.tagService = tagService;
	}

	// Buscar todos
	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	public List<Report> findAllByCreatorContactPostcode(String postcode){
		return reportRepository.findAllByCreatorContactPostcode(postcode);
	}

	public List<Report> findAllByOrderByCreationDesc(){
		return reportRepository.findAllByOrderByCreationDesc();
	}

	public List<Report> findAllByTagsIdOrderByCreationDesc(Long tagId){
		return reportRepository.findAllByTagsIdOrderByCreationDesc(tagId);
	}

	public List<Report> findAllByTagsId(Long id){
		return reportRepository.findAllByTagsId(id);
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

	// Editar Reporte
	public Report updateReport(Report existingReport, Report updatedReport) {
		if (existingReport != null && updatedReport != null) {

			existingReport.setTitle(updatedReport.getTitle());
			existingReport.setDescription(updatedReport.getDescription());
			/*
			 * existingReport.setMunicipality(updatedReport.getMunicipality());
			 * existingReport.setLocation(updatedReport.getLocation());
			 */

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
}
