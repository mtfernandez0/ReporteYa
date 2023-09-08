package com.cons.reporteya.controller;

import java.security.Principal;

import com.cons.reporteya.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cons.reporteya.entity.Marker;
import com.cons.reporteya.entity.Report;
import com.cons.reporteya.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.cons.reporteya.service.UserService;

import java.security.Principal;

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
    public String newReport(@ModelAttribute("marker") Marker marker,
                            @ModelAttribute("report") Report report,
                            RedirectAttributes attributes,
                            Model model){
        if (marker.getLatitude() == null || marker.getLongitude() == null){
            attributes.addFlashAttribute(
                    "mapInvalidCoo",
                    true
            );
            return "redirect:/map";
        }

        String location = "";

        if (marker.getCity() != null) location += marker.getCity();
        else if(marker.getTown() != null) location += marker.getTown();
        else if(marker.getVillage() != null) location += marker.getVillage();
        else location += marker.getSuburb();

        String finalLocation = "";

        if (marker.getRoad() != null) finalLocation += marker.getRoad() + ", ";

        finalLocation = String.format("%s%s, %s", finalLocation, location, marker.getCountry());
        model.addAttribute("location", finalLocation);

        return "report/new";
    }

    @PostMapping("/new")
    public String newReport(@ModelAttribute("marker") Marker marker,
                            @Valid @ModelAttribute("report") Report report,
                            BindingResult result,
                            Principal principal){

        if (result.hasErrors()) return "report/new";

        User user = userService.findByEmail(principal.getName());

        report.setCreator(user);

        reportService.createReport(report);

        return "redirect:/map";
    }

    @GetMapping("")
    public String reports (Model model, Principal principal ) {
    	model.addAttribute("reports",reportService.findAll());
    	model.addAttribute("user",userService.findByEmail(principal.getName()));
    	return "report/reports";
    }

}
