package com.cons.reporteya.controller;

import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final ReportService reportService;

    public HomeController(UserService userService,
                          ReportService reportService) {
        this.userService = userService;
        this.reportService = reportService;
    }

    @GetMapping(value= {"", "/"})
    public String redirect(){
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/map")
    public String map(Model model){
        model.addAttribute("reports", reportService.findAll());
        return "map";
    }
}
