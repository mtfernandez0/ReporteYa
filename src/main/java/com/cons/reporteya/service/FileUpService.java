package com.cons.reporteya.service;

import com.cons.reporteya.entity.Report;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cons.reporteya.entity.FileUp;
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
}
