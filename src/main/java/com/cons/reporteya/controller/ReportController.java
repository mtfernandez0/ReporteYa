package com.cons.reporteya.controller;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/new")
    public String newReport(@ModelAttribute("marker") Marker marker,
                            @ModelAttribute("report") Report report,
                            RedirectAttributes attributes){
        if (marker.getLatitude() == null || marker.getLongitude() == null){
            attributes.addFlashAttribute(
                    "mapInvalidCoo",
                    true
            );
            return "redirect:/map";
        }
        return "report/new";
    }
}
