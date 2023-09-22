package com.cons.reporteya.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.cons.reporteya.dto.CompanyDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cons.reporteya.entity.Company;
import com.cons.reporteya.entity.FileUp;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.CompanyService;
import com.cons.reporteya.service.FileUpService;
import com.cons.reporteya.service.UserService;

import jakarta.validation.Valid;

@Controller
public class CompanyController {

	private final CompanyService companyService;
	private final UserService userServ;
	private final FileUpService fileupService;
	public CompanyController(CompanyService companyService,
							 UserService user,
							 FileUpService fileupService) {
		this.companyService = companyService;
		this.userServ = user;
		this.fileupService = fileupService;
	}
	
	private String UPLOAD_FOLDER = "src/main/resources/static/images/companyLogos";

	@GetMapping("/companies")
	public String companies(Model model){

		List<Company> companies = companyService.findAll();
		List<List<CompanyDto>> companiesGroup = new ArrayList<>();

		int i = 0;
		for (int j = 0; j < companies.size(); j++) {
			if (j % 4 == 0){
				i++; companiesGroup.add(new ArrayList<>());
			}
			companiesGroup.get(i - 1).add(CompanyDto.companyToCompanyDto(companies.get(j)));
		}

		model.addAttribute("companiesGroup", companiesGroup);

		return "company/dashboard";
	}

	@GetMapping("/companies/new")
	public String companynew(@ModelAttribute("company") Company company) {
		return "company/company";
	}
	
	@PostMapping("/companies/new")
		public String company(@Valid @ModelAttribute("company") Company company,BindingResult bindingResult,Principal principal, @RequestParam(value = "files", required = false) MultipartFile file) {
		if(bindingResult.hasErrors()) return "company/company";

		User us = userServ.findByEmail(principal.getName());
		company.setUser(us);
		companyService.save(company);

		FileUp fileUp =  fileupService.subirFotoDePerfil(file, company, UPLOAD_FOLDER);
		try {
			byte[] bytes = file.getBytes();
			Path ruta = Paths.get(UPLOAD_FOLDER, fileUp.getNombre());
			Files.write(ruta, bytes);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/home";
		
		}
	}
