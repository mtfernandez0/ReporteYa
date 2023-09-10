package com.cons.reporteya.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

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

	// Crear Reporte
	public void createReport(Report report, List<String> subjects) {
		List<Tag> tags = generateTagList(subjects);

		tags.forEach(tag -> {
			if (tag.getId() == null) {
				tagService.saveTag(tag);
			}
		});

		report.setTags(tags);
		reportRepository.save(report);
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

	// Editar Reporte
	public Report updateReport(Report existingReport, Report updatedReport) {
		if (existingReport != null && updatedReport != null) {

			existingReport.setTitle(updatedReport.getTitle());
			existingReport.setDescription(updatedReport.getDescription());
			existingReport.setMunicipality(updatedReport.getMunicipality());
			existingReport.setLocation(updatedReport.getLocation());

			return reportRepository.save(existingReport);

		} else {

			throw new IllegalArgumentException("El reporte no puede ser nulo.");
		}
	}

	// Borrar Reporte
	public void deleteReport(Long id) {
		if (reportRepository.existsById(id)) {
			reportRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("No se encontró el reporte a eliminar.");
		}
	}

}
