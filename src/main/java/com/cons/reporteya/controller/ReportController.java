package com.cons.reporteya.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cons.reporteya.entity.Comment;
import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.CommentService;
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

	public ReportController(ReportService reportService, UserService userService, MarkerService markerService,
			CommentService commentService) {
		this.reportService = reportService;
		this.userService = userService;
		this.markerService = markerService;
		this.commentService = commentService;
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
							@RequestParam("tag") String tags) {

		List<String> tagList = Arrays.asList(tags.split(","));
		checkTagErrors(result, tagList);

		if (result.hasErrors()) {
			return "report/new";
		}

		User user = userService.findByEmail(principal.getName());

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

	@PostMapping("/dashboard")
	public String addComment(@RequestParam Long id, @RequestParam String comment, Model model, Principal principal) {
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

			return "redirect:/reports/dashboard";
		} else {
			model.addAttribute("errorMessage", "El informe no se encontró");
			return "report/reports";
		}
	}

	@GetMapping("")
	public String reports(Model model, Principal principal) {
		model.addAttribute("reports", reportService.findAll());
		model.addAttribute("user", userService.findByEmail(principal.getName()));
		return "report/reports";
	}

}
