package com.cons.reporteya.service;

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
	
	public FileUp subirArchivoABD(MultipartFile archivo) {
		
		FileUp nuevoArchivo = FileUp.builder()
				.nombre(archivo.getOriginalFilename())
				.fileType(archivo.getContentType())
				.rutaArchivo("/src/main/resources/static/images/"+archivo.getOriginalFilename())
				.build();
		
		//fileupRepo.save(nuevoArchivo);
		
		/*if(nuevoArchivo != null) {
			System.out.println(archivo.getOriginalFilename());
			return "Upload exitoso" + archivo.getOriginalFilename(); 
		}*/
		
		return fileupRepo.save(nuevoArchivo);
		
	}
	

}
