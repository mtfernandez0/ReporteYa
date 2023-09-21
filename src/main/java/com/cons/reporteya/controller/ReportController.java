package com.cons.reporteya.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.cons.reporteya.entity.*;
import com.cons.reporteya.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
	private final CommentService commentService;
	private final TagService tagService;
	private final FileUpService fileupService;

	private String UPLOAD_FOLDER = "src/main/resources/static/images/reports";

	public ReportController(ReportService reportService,
							UserService userService,
							MarkerService markerService,
							CommentService commentService,
							TagService tagService,
							FileUpService fuS) {
		this.reportService = reportService;
		this.userService = userService;
		this.markerService = markerService;
		this.commentService = commentService;
		this.tagService = tagService;
		this.fileupService = fuS;

	}

	@GetMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker,
							@ModelAttribute("report") Report report,
							RedirectAttributes attributes) {

		if (marker.getLatitude() == null || marker.getLongitude() == null) {
			attributes.addFlashAttribute("mapInvalidCoo", true);
			return "redirect:/map";
		}

		return "report/new";
	}

	@PostMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker,
							@Valid @ModelAttribute("report") Report report,
							BindingResult result,
							Principal principal,
							@RequestParam(value = "tag", required = false) String tags,
							@RequestParam(value = "files", required = false) MultipartFile[] files) {

		List<String> tagList = tags == null ? new ArrayList<>() : Arrays.stream(tags.split(","))
				.map(String::trim).collect(Collectors.toList());

		checkImgErrors(result, files);
		checkTagErrors(result, tagList);

		if (result.hasErrors()) return "report/new";

		User user = userService.findByEmail(principal.getName());

		report.setCreator(user);
		report = reportService.createReport(report, tagList);

		for (MultipartFile file : files) {

			if (Objects.equals(file.getOriginalFilename(), "")) break;

			FileUp fileUp =  fileupService.subirArchivoABD(file, report, UPLOAD_FOLDER);
			try {
				byte[] bytes = file.getBytes();
				Path ruta = Paths.get(UPLOAD_FOLDER, fileUp.getNombre());
				Files.write(ruta, bytes);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}

		marker.setReport(report);
		markerService.save(marker);
		
		return "redirect:/reports";
	}

	private void checkImgErrors(BindingResult result, MultipartFile[] files){
		if(files.length > 5)
			result.rejectValue(
					"images",
					"Maximo de 5 imagenes",
					"Solo podes ingresar hasta 5 imágenes"
			);

		else if(files.length == 0)
			result.rejectValue(
					"images",
					"Minimo una imagen",
					"Debes de incluir al menos una imagen"
			);
	}

	private void checkTagErrors(BindingResult result, List<String> subjects) {
		boolean areSizeCorrect = true;

		for (String subject : subjects) areSizeCorrect &= subject.length() < 40;

		if (!areSizeCorrect)
			result.rejectValue("tags", "size", "Los tags deben tener como mucho 40 carácteres");

		if (subjects.size() > 5)
			result.rejectValue("tags", "Maximum of 5 tags", "Solo puedes incluir hasta 5 tags");
	}

	@GetMapping("/dashboard")
	public String reports(Model model) {
		List<Report> reportes = reportService.findAll();
		model.addAttribute("reports", reportes);
		return "report/reports";
	}

	@PostMapping("/dashboard")
	public String addComment(@RequestParam Long id,
							 @RequestParam String comment,
							 Model model,
							 Principal principal) {
		Optional<Report> reportOptional = reportService.findById(id);

		if (reportOptional.isPresent()) {
			Report report = reportOptional.get();

			Comment newComment = Comment.builder().comment(comment).owner(userService.findByEmail(principal.getName()))
					.report(report).build();

			commentService.save(newComment);
			report.getComments().add(newComment);
			reportService.updateReport(report, report);

			List<Comment> updatedComments = report.getComments();
			model.addAttribute("comments", updatedComments);

		} else {
			model.addAttribute("errorMessage", "El informe no se encontró");
		}
		return "redirect:/reports";
	}
	@PostMapping("/dashboardEmp")
	public String addCommentEmpresa(@RequestParam Long id,
									@RequestParam String comment,
									Model model,
									Principal principal) {
		Optional<Report> reportOptional = reportService.findById(id);

		if (reportOptional.isPresent()) {
			Report report = reportOptional.get();
			User usu = userService.findByEmail(principal.getName());

			if (usu.getCompany() == null) return "redirect:/home";

			Comment newComment = Comment.builder().comment(comment).owner(usu).report(report).build();
			newComment.setCompany(usu.getCompany());

			commentService.save(newComment);
			report.getComments().add(newComment);
			reportService.updateReport(report, report);

			List<Comment> updatedComments = report.getComments();
			model.addAttribute("comments", updatedComments);

		} else {
			model.addAttribute("errorMessage", "El informe no se encontró");
		}
		return "redirect:/reports";
	}

	@GetMapping("")
	public String reports(Model model, Principal principal) {
		model.addAttribute("reports", reportService.findAllByOrderByCreationDesc());
		model.addAttribute("user", userService.findByEmail(principal.getName()));
		model.addAttribute("tagList", tagService.findAllOrderBySubjectCount());
		return "report/reports";
	}

	@GetMapping("/tags/{id}")
	public String reportsByTag(@PathVariable Long id,
							   Model model,
							   Principal principal) {

		if (tagService.findById(id).isEmpty()) return "redirect:/reports";

		model.addAttribute("reports", reportService.findAllByTagsIdOrderByCreationDesc(id));
		model.addAttribute("user", userService.findByEmail(principal.getName()));
		model.addAttribute("tagList", tagService.findAllOrderBySubjectCount());

		return "report/reports";
	}
}
