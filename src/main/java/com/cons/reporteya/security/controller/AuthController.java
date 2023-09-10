package com.cons.reporteya.security.controller;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.entity.VerificationToken;
import com.cons.reporteya.security.config.emailValidation.OnRegistrationCompleteEvent;
import com.cons.reporteya.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class AuthController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageSource messages;

    public AuthController(UserService userService,
                          ApplicationEventPublisher eventPublisher,
                          MessageSource messages) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.messages = messages;
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

    @GetMapping("/registerConfirm")
    public String registerConfirm(WebRequest webRequest,
                                      Model model,
                                      @RequestParam String token){
        Locale locale = webRequest.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "auth/badUser";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            String messageValue = messages.getMessage("auth.message.expired", null, locale);
            model.addAttribute("message", messageValue);
            return "auth/badUser";
        }

        user.setEnabled(true);
        // In order to avoid not null exception
        user.setPasswordConfirmation(user.getPassword());
        userService.save(user);
        return "redirect:/login";
    }

    @PostMapping("/register")
    public String validate(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes){

        userService.checkCredentialsRegistration(user, result);

        if (result.hasErrors()) return "auth/register";

        user = userService.register(user);

        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
                    user,
                    request.getLocale(),
                    appUrl)
            );
        } catch (RuntimeException ex) {
            result.rejectValue(
                    "email",
                    "Invalid"
            );
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute(
                "register",
                "Registrado exitosamente!"
        );

        return "redirect:/login";
    }
}