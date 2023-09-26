package com.cons.reporteya.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import com.cons.reporteya.entity.*;
import com.cons.reporteya.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@Value("${imagePath}")
	private String imageDir;

	public ReportController(ReportService reportService, UserService userService, MarkerService markerService,
			CommentService commentService, TagService tagService, FileUpService fuS) {
		this.reportService = reportService;
		this.userService = userService;
		this.markerService = markerService;
		this.commentService = commentService;
		this.tagService = tagService;
		this.fileupService = fuS;

	}

	/**
	 * Method in charge of displaying all reports by pages
	 * @param model
	 * @param principal
	 * @param request
	 * @param page
	 * @return
	 */
	@GetMapping(value = { "/{page}", "" })
	public String reports(Model model,
						  Principal principal,
						  HttpServletRequest request,
						  @PathVariable(required = false) Integer page) {

		if (page == null || page < 0) page = 0;

		User user = userService.findByEmail(principal.getName());
		Page<Report> reports = reportService.reportsPerPage(page);
		model.addAttribute("tagId", null);
		model.addAttribute("currentPage", page);

		generateModelForDashboard(model, reports, request, user);

		return "report/reports";
	}

	/**
	 * Method in charge of displaying all reports by tags
	 * @param id
	 * @param page
	 * @param request
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping(value = { "/tags/{id}", "/tags/{id}/{page}" })
	public String reportsByTag(@PathVariable Long id,
							   @PathVariable(required = false) Integer page,
							   HttpServletRequest request,
							   Model model,
							   Principal principal) {

		User user = userService.findByEmail(principal.getName());

		if (page == null || page < 0) page = 0;

		if (tagService.findById(id).isEmpty())
			return "redirect:/reports";

		model.addAttribute("tagId", id);
		model.addAttribute("currentPage", page);

		Page<Report> reports = reportService.findAllByTagsIdOrderByCreationDesc(id, page);

		generateModelForDashboard(model, reports, request, user);

		return "report/reports";
	}

	/**
	 * Will display the view of creating a new report
	 * @param marker
	 * @param report
	 * @param attributes
	 * @return
	 */
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

	/**
	 * Endpoing in charge of creating a new report
	 * @param marker
	 * @param report
	 * @param result
	 * @param principal
	 * @param tags
	 * @param files
	 * @return
	 */
	@PostMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker,
							@Valid @ModelAttribute("report") Report report,
							BindingResult result,
							Principal principal,
							@RequestParam(value = "tag", required = false) String tags,
							@RequestParam(value = "files", required = false) MultipartFile[] files) {

		List<String> tagList = tags == null ? new ArrayList<>()
				: Arrays.stream(tags.split(",")).map(String::trim).collect(Collectors.toList());

		checkImgErrors(result, files, 0);
		checkTagErrors(result, tagList);

		if (result.hasErrors()) return "report/new";

		User user = userService.findByEmail(principal.getName());

		//In case the user managed to get here illegally
		if (!userService.canCreateAReport(user)) return "redirect:/reports/user";

		report.setCreator(user);
		report = reportService.createReport(report, tagList);

		for (MultipartFile file : files) {

			if (Objects.equals(file.getOriginalFilename(), ""))
				break;

			FileUp fileUp = fileupService.subirArchivoABD(file, report, imageDir);
			try {
				byte[] bytes = file.getBytes();
				Path ruta = Paths.get(imageDir, fileUp.getNombre());
				Files.write(ruta, bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		marker.setReport(report);
		markerService.save(marker);

		return "redirect:/reports";
	}

	/**
	 * Method in charge of adding a comment to a specific report as a user
	 * @param id
	 * @param comment
	 * @param model
	 * @param principal
	 * @return
	 */
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

	/**
	 * Method in charge of adding a comment to a specific report as a company
	 * @param id
	 * @param comment
	 * @param model
	 * @param principal
	 * @return
	 */
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

	/**
	 * Method in charge of displaying all reports of a user
	 * @param model
	 * @param principal
	 * @param request
	 * @return
	 */
	@GetMapping("/user")
	public String myReport(Model model,
						   Principal principal,
						   HttpServletRequest request) {

		User us = userService.findByEmail(principal.getName());

		model.addAttribute("reports", us.getReports());
		model.addAttribute("request", request);
		model.addAttribute("pages", 1);
		model.addAttribute("user", us);
		model.addAttribute("tagList", tagService.findAllOrderBySubjectCount());
		model.addAttribute("currentPage", 0);

		return "report/reports";
	}

	/**
	 * Edit page of a particular report
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String viewEdit(@PathVariable Long id,
						   Model model) {

		Optional<Report> rep = reportService.findById(id);

		if (rep.isEmpty()) return "redirect:/home";

		model.addAttribute("report", rep.get());

		return "report/editReport";
	}

	/**
	 * Method in charge of updating a specific report
	 * @param report
	 * @param result
	 * @param principal
	 * @param tags
	 * @param files
	 * @param idreport
	 * @return
	 */
	@PutMapping("/edit/{id}")
	public String editReport(@Valid @ModelAttribute("report") Report report, BindingResult result, Principal principal,
			@RequestParam(value = "tag", required = false) String tags,
			@RequestParam(value = "files", required = false) MultipartFile[] files, @PathVariable("id") Long idreport) {

		List<String> tagList = tags == null ? new ArrayList<>()
				: Arrays.stream(tags.split(",")).map(String::trim).collect(Collectors.toList());

		checkImgErrors(result, files, 1);
		checkTagErrors(result, tagList);

		if (result.hasErrors())
			return "report/new";

		Report newReport = reportService.findById(idreport).get();

		if (!Objects.equals(files[0].getOriginalFilename(), "")) {

			List<FileUp> oldImages = newReport.getImages();
			for (FileUp img : oldImages) {
				File miFichero = new File(img.getRutaArchivo());
				miFichero.delete();
			}
			fileupService.deleteAllByReporteId(newReport.getId());
			newReport.getImages().clear();
		}

		newReport.setTitle(report.getTitle());
		newReport.setDescription(report.getDescription());
		newReport.setAdditional_directions(report.getAdditional_directions());
		newReport = reportService.createReport(newReport, tagList);

		for (MultipartFile file : files) {

			if (Objects.equals(file.getOriginalFilename(), ""))
				break;

			FileUp fileUp = fileupService.subirArchivoABD(file, newReport, imageDir);

			try {
				byte[] bytes = file.getBytes();
				Path ruta = Paths.get(imageDir, fileUp.getNombre());
				Files.write(ruta, bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return "redirect:/reports";
	}

	/**
	 * Endpoint in charge of deleting a specific report
	 * @param reportId
	 * @return
	 */
	@DeleteMapping("/delete/{reportId}")
	public String deleteReport(@PathVariable Long reportId) {
		reportService.deleteReportById(reportId);
		return "redirect:/reports/user";
	}

	/**
	 * This method will check for errors in the files passed as parameters
	 * @param result
	 * @param files
	 * @param img
	 */
	private void checkImgErrors(BindingResult result,
								MultipartFile[] files,
								int img) {
		if (files.length > 5)
			result.rejectValue(
					"images",
					"Maximo de 5 imagenes",
					"Solo podes ingresar hasta 5 imágenes");

		if (img == 0 && files.length == 0)
			result.rejectValue(
					"images",
					"Minimo una imagen",
					"Debes de incluir al menos una imagen");
	}

	/**
	 * This method will check the existence of errors in tags
	 * @param result
	 * @param subjects
	 */
	private void checkTagErrors(BindingResult result,
								List<String> subjects) {
		boolean areSizeCorrect = true;

		for (String subject : subjects)
			areSizeCorrect &= subject.length() < 40;

		if (!areSizeCorrect)
			result.rejectValue(
					"tags",
					"size",
					"Los tags deben tener como mucho 40 carácteres"
			);

		if (subjects.size() > 5)
			result.rejectValue(
					"tags",
					"Maximum of 5 tags",
					"Solo puedes incluir hasta 5 tags"
			);
	}

	/**
	 * Method in charge of filling the model with the appropriate attributes required
	 * for the dashboard
	 * @param model
	 * @param reports
	 * @param request
	 * @param user
	 */
	private void generateModelForDashboard(Model model,
										   Page<Report> reports,
										   HttpServletRequest request,
										   User user) {

		model.addAttribute("reports", reports);
		model.addAttribute("request", request);
		model.addAttribute("pages", reports.getTotalPages());
		model.addAttribute("user", user);
		model.addAttribute("tagList", tagService.findAllOrderBySubjectCount());
	}
}
