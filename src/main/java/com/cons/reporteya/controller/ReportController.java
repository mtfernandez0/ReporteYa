package com.cons.reporteya.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.FileUpService;
import com.cons.reporteya.service.MarkerService;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/reports")
public class ReportController {

	private final ReportService reportService;
	private final UserService userService;
	private final MarkerService markerService;
	private final FileUpService fileupService;

	public ReportController(ReportService reportService, UserService userService, MarkerService markerService,FileUpService fuS) {
		this.reportService = reportService;
		this.userService = userService;
		this.markerService = markerService;
		this.fileupService = fuS;
	}
	private String UPLOAD_FOLDER = "src/main/resources/static/images";

	@GetMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker,
							@ModelAttribute("report") Report report,
							RedirectAttributes attributes,
							Model model,
							Principal principal) {

		User user = userService.findByEmail(principal.getName());

		if (user.getContact() == null){
			attributes.addFlashAttribute("newReportNoContact", true);
			return "redirect:/profile";
		}

		if (marker.getLatitude() == null || marker.getLongitude() == null) {
			attributes.addFlashAttribute("mapInvalidCoo", true);
			return "redirect:/map";
		}

		report.setMarker(marker);

		return "report/new";
	}

	@PostMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker, @Valid @ModelAttribute("report") Report report,
			BindingResult result, Principal principal, @RequestParam("tag") String tags, @RequestParam("imagen") MultipartFile [] files) {

		if(files.length>5) 
			result.rejectValue("imagen", "Maximo de 5 imagenes", "Solo podes ingresar 5 imagenes");
		
		else if(files.length==0) 
			result.rejectValue("imagen", "Minimo una imagen", "Debes incluir al menos una imagen");
		
		
		List<String> tagList = Arrays.asList(tags.split(","));
		checkTagErrors(result, tagList);

		if (result.hasErrors()) {
			return "report/new";
		}
		
		User user = userService.findByEmail(principal.getName());
		
		for (MultipartFile file:files ) {
			report.getImagenes().add(fileupService.subirArchivoABD(file));
			try {
				byte[] bytes = file.getBytes();
				Path ruta = Paths.get(UPLOAD_FOLDER, file.getOriginalFilename());
				Files.write(ruta, bytes);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
		

		report.setCreator(user);

		report = reportService.createReport(report, tagList);
		marker.setReport(report);
		markerService.save(marker);
		
		

		return "redirect:/reports";
	}

	private void checkTagErrors(BindingResult result, List<String> subjects) {
		boolean areSizeCorrect = true;

		for (String subject : subjects)
			areSizeCorrect &= subject.length() < 140;

		if (!areSizeCorrect)
			result.rejectValue("tags", "size", "Tags must have at most 140 characters");

		if (subjects.size() > 5)
			result.rejectValue("tags", "Maximum of 5 tags", "You can only include up to 5 tags");
	}

	@GetMapping("/dashboard")
	public String reports(Model model) {
		List<Report> reportes = reportService.findAll();
		model.addAttribute("reports", reportes);
		return "report/reports";
	}

	@GetMapping("")
	public String reports(Model model, Principal principal) {
		model.addAttribute("reports", reportService.findAll());
		model.addAttribute("user", userService.findByEmail(principal.getName()));
		return "report/reports";
	}

}
