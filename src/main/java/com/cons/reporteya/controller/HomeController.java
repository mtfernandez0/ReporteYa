package com.cons.reporteya.controller;

import com.cons.reporteya.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    
    
    @GetMapping(value= {"", "/"})
    public String redirect(){
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
