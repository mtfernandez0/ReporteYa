package com.cons.reporteya.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;
    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newReport(@ModelAttribute("coordinate") Marker coordinate,
                            @ModelAttribute("report") Report report){
        return "report/new";
    }
    
    @GetMapping("")
    public String reports (Model model, Principal principal ) {
    	model.addAttribute("reports",reportService.findAll());
    	model.addAttribute("user",userService.findByEmail(principal.getName()));
    	return "report/reports";
    }
    
}
