package com.cons.reporteya.service;

import java.util.List;

import com.cons.reporteya.entity.Marker;
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

	public static String finalLocation(Report report){
		Marker marker = report.getMarker();
		String location = "";

		if (marker.getCity() != null) location += marker.getCity();
		else if(marker.getTown() != null) location += marker.getTown();
		else if(marker.getVillage() != null) location += marker.getVillage();
		else location += marker.getSuburb();

		String res = "";

		if (marker.getRoad() != null) res += marker.getRoad() + ", ";

		return String.format("%s%s, %s", res, location, marker.getCountry());
	}

}
