package com.cons.reporteya.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cons.reporteya.entity.Report;
import com.cons.reporteya.repository.ReportRepository;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;

@Controller
public class HomeController {

	private final UserService userService;
	private final ReportService reportService;
	private final ReportRepository reportRepository;

	public HomeController(UserService userService, ReportService reportService, ReportRepository reportRepository) {
		this.userService = userService;
		this.reportService = reportService;
		this.reportRepository = reportRepository;
	}

	@GetMapping(value = { "", "/" })
	public String redirect() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@GetMapping("/map")
	public String map(Model model) {
		model.addAttribute("reports", reportService.findAll());
		return "map";
	}

}
