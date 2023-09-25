package com.cons.reporteya.controller;

import java.security.Principal;

import com.cons.reporteya.dto.ContactDto;
import com.cons.reporteya.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cons.reporteya.entity.Contact;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.ContactService;
import com.cons.reporteya.service.UserService;

@Controller
public class UserController {

	private final UserService userServ;
	private final ContactService contServ;
	private final ReportService reportService;

	public UserController(UserService uSer, ContactService cServ, ReportService reportService) {
		this.userServ = uSer;
		this.contServ = cServ;
		this.reportService = reportService;
	}

	@GetMapping("/profile")
	public String showProfile(Model model,
							  Principal principal) {
								
		User us = userServ.findByEmail(principal.getName());
		model.addAttribute("user", userServ.findByEmail(principal.getName()));

		if (us.getContact() != null)
			model.addAttribute("contact", us.getContact());

		return "count/profile";
	}

	@PostMapping("/profile")
	public ResponseEntity<?> formProfile(@RequestBody ContactDto contactDto,
										 Principal principal) {

		User us = userServ.findByEmail(principal.getName());

		System.out.println("PASO");

		if (us.getContact() != null) return ResponseEntity.badRequest().build();

		Contact contact = contactDto.contactDtoToContact();

		contact.setUser(us);
		contServ.newContact(contact);

		return ResponseEntity.ok().build();
	}

	@PutMapping("/profile")
	public ResponseEntity<?> editProfile(@RequestBody ContactDto contactDto,
										 Principal principal) {
		User us = userServ.findByEmail(principal.getName());

		if (us.getContact() == null) ResponseEntity.badRequest().build();

		us.getContact().setCountry(contactDto.getCountry());
		us.getContact().setPostcode(contactDto.getPostcode());
		us.getContact().setLocation_name(contactDto.getLocation_name());

		contServ.editContact(us.getContact());

		reportService.deleteAllByCreatorId(us.getId());

		us.getReports().clear();

		return ResponseEntity.ok().build();
	}
}
