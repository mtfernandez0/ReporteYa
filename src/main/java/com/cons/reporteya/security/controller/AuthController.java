package com.cons.reporteya.security.controller;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(HttpSession session,
                        Model model) {
        if (session.getAttribute("email") != null){
            model.addAttribute("email", session.getAttribute("email"));
            session.removeAttribute("email");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") User user) { return "auth/register"; }

    @PostMapping("/register")
    public String validate(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           RedirectAttributes redirectAttributes){

        userService.checkCredentialsRegistration(user, result);

        if (result.hasErrors()) return "auth/register";

        userService.register(user);

        redirectAttributes.addFlashAttribute(
                "register",
                "Registration successfully!"
        );

        return "redirect:/login";
    }
}