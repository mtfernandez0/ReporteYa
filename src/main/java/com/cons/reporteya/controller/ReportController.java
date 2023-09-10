package com.cons.reporteya.controller;

import java.security.Principal;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.repository.ReportRepository;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;
    private final MarkerService markerService;

    public ReportController(ReportService reportService,
                            UserService userService,
                            MarkerService markerService) {
        this.reportService = reportService;
        this.userService = userService;
        this.markerService = markerService;
    }

	@GetMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker, @ModelAttribute("report") Report report,
			RedirectAttributes attributes, Model model) {
		if (marker.getLatitude() == null || marker.getLongitude() == null) {
			attributes.addFlashAttribute("mapInvalidCoo", true);
			return "redirect:/map";
		}

        report.setMarker(marker);
        ReportService.finalLocation(report);

        model.addAttribute("location", report);

		return "report/new";
	}

	@PostMapping("/new")
	public String newReport(@ModelAttribute("marker") Marker marker, @Valid @ModelAttribute("report") Report report,
			BindingResult result, Principal principal, @RequestParam String tags) {
		List<String> tagList = Arrays.asList(tags.split(","));
		reportService.createReport(report, tagList);

		return "redirect:/map";
	}

	@GetMapping("/dashboard")
	public String reports(Model model) {
		List<Report> reportes = reportRepository.findAll();
		model.addAttribute("reports", reportes);
		return "report/reports";
	}

        User user = userService.findByEmail(principal.getName());

        report.setCreator(user);
        marker.setReport(reportService.createReport(report));
        markerService.save(marker);

        return "redirect:/reports";
    }

    @GetMapping("")
    public String reports (Model model, Principal principal ) {
    	model.addAttribute("reports",reportService.findAll());
    	model.addAttribute("user",userService.findByEmail(principal.getName()));
    	return "report/reports";
    }

}
