package com.cons.reporteya.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cons.reporteya.service.FileUpService;

@RestController
public class FileUpController {
	
	private final FileUpService fileupService;
	public FileUpController(FileUpService fuS) {
		this.fileupService = fuS;
	}

	@Value("${imagePath}")
	private String imageDir;
//	private String UPLOAD_FOLDER = "src/main/resources/static/images";
	
//	@PostMapping("/subir")
//	public String subirArchivo(@RequestParam("imagen") MultipartFile [] archivo) {
//		if(archivo == null) {
//			throw new RuntimeException ("Por favor subir un archivo");
//		}
//
//		System.out.println("Tamaño del archivo" + archivo.length);
//
//		fileupService.subirArchivoABD(archivo);
//
//		try {
//			byte[] bytes = archivo.getBytes();
//			Path ruta = Paths.get(UPLOAD_FOLDER, archivo.getOriginalFilename());
//			Files.write(ruta, bytes);
//		}catch(IOException e) {
//			e.printStackTrace();
//		}
//
//
//		return "TODO SALIO BIEN!!";
//
//	}

}
