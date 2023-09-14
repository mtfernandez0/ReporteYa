package com.cons.reporteya.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.CompanyService;
import com.cons.reporteya.service.UserService;

import jakarta.validation.Valid;

@Controller
public class CompanyController {

	private final CompanyService companyService;
	private final UserService userServ;
	public CompanyController(CompanyService companyService,
							 UserService uSer) {
		this.companyService = companyService;
		this.userServ = uSer;		
	}

	@GetMapping("/companies")
	public String companies(Model model){
		model.addAttribute("companies", companyService.findAll());

		return "company/dashboard";
	}

	@GetMapping("/companies/new")
	public String companynew(@ModelAttribute("company") Company company) {
		return "company/company";
	}
	
	@PostMapping("/companies/new")
		public String company(@Valid @ModelAttribute("company") Company company,BindingResult bindingResult,Principal principal) {
		if(bindingResult.hasErrors())
		{
			return "company/company";
		}
		User us = userServ.findByEmail(principal.getName());	
		company.setUser(us);
		companyService.save(company);
		
		return "redirect:/home";
		
		}
	}
	

