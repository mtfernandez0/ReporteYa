package com.cons.reporteya.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.entity.FileUp;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.repository.FileUpRepo;

@Service
public class FileUpService {
	
	private final FileUpRepo fileupRepo;
	public FileUpService(FileUpRepo fuR) {
		this.fileupRepo = fuR;
	}
	
	public FileUp subirArchivoABD(MultipartFile archivo,
								  Report report,
								  String path) {

		String fileName =
				report.getCreator().getEmail() + archivo.getOriginalFilename();

		FileUp nuevoArchivo = FileUp.builder()
				.nombre(fileName)
				.fileType(archivo.getContentType())
				.rutaArchivo(path + "/" + fileName)
				.build();

		nuevoArchivo.setReporte(report);

		return fileupRepo.save(nuevoArchivo);
	}
	public FileUp subirFotoDePerfil(MultipartFile archivo,
									Company company,
									String path) {
		
		String fileName =
				company.getName() + archivo.getOriginalFilename();
		
		FileUp nuevoArchivo = FileUp.builder()
		.nombre(fileName)
		.fileType(archivo.getContentType())
		.rutaArchivo(path + "/" + fileName)
		.build();
		
		nuevoArchivo.setCompany(company);
		
		return fileupRepo.save(nuevoArchivo);
		}
	public void borrar(Long id) {
		 fileupRepo.deleteById(id);
	}
	
	public void deleteAll(List<FileUp> files){
        fileupRepo.deleteAll(files);
    }
	@Transactional
	public void deleteAllByReporteId(Long report_id) {
        fileupRepo.deleteAllByReporteId(report_id);
    }
	
}
