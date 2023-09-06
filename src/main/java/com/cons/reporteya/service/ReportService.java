package com.cons.reporteya.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.repository.ReportRepository;

@Service
public class ReportService {

	private final ReportRepository reportRepository;

	public ReportService(ReportRepository reportRepository) {
		this.reportRepository = reportRepository;
	}

	public List<Report> findAll() {
		return reportRepository.findAll();
	}

	// Crear Reporte
	public Report createReport(Report r) {
		return reportRepository.save(r);
	}

	// Editar Reporte
	public Report updateReport(Report existingReport, Report updatedReport) {
		if (existingReport != null && updatedReport != null) {

			existingReport.setTitle(updatedReport.getTitle());
			existingReport.setDescription(updatedReport.getDescription());
			existingReport.setMunicipality(updatedReport.getMunicipality());
			existingReport.setLocation(updatedReport.getLocation());
			existingReport.setBudget(updatedReport.getBudget());

			return reportRepository.save(existingReport);

		} else {

			throw new IllegalArgumentException("El reporte no puede ser nulo.");
		}
	}

	// Borrar Reporte
	public void deleteReport(Long id) {
		if (reportRepository.existById(id)) {
			reportRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("No se encontró el reporte a eliminar.");
		}
	}

}
