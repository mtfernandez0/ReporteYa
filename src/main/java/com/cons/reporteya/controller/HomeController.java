package com.cons.reporteya.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.cons.reporteya.dto.ContactDto;
import com.cons.reporteya.dto.ReportDto;
import com.cons.reporteya.dto.UserDto;
import com.cons.reporteya.entity.Report;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.ReportService;
import com.cons.reporteya.service.UserService;

import com.cons.reporteya.repository.ReportRepository;

@Controller
public class HomeController {

	private final UserService userService;
	private final ReportService reportService;

	public HomeController(UserService userService,
						  ReportService reportService) {
		this.userService = userService;
		this.reportService = reportService;
	}

	@GetMapping(value = { "", "/" })
	public String redirect() {
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(Model model,Principal principal ) {
		User us = userService.findByEmail(principal.getName());
		model.addAttribute("usuario",us);
		return "home";
	}

	@GetMapping("/map")
	public String map(Model model,
					  Principal principal) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<ReportDto> reportDtos = new ArrayList<>();
		List<Report> reports = reportService.findAll();
		for (Report report : reports) reportDtos.add(ReportDto.ReportToDto(report));

		User user = userService.findByEmail(principal.getName());

		ContactDto contactDto = new ContactDto().contactToContactDto(user.getContact());
//		UserDto userDto = new UserDto().userToUserDto(user);

//		model.addAttribute("user", objectMapper.writeValueAsString(userDto));
		model.addAttribute("contact", objectMapper.writeValueAsString(contactDto));
		model.addAttribute("reports", objectMapper.writeValueAsString(reportDtos));
		return "map";
	}

}
