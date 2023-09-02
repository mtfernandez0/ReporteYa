package com.cons.reporteya.controller;

import com.cons.reporteya.entity.Coordinate;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/new")
    public String newReport(@ModelAttribute("coordinate") Coordinate coordinate,
                            @ModelAttribute("report") Report report){
        return "report/new";
    }
}
