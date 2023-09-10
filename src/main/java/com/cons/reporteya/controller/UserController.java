package com.cons.reporteya.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.cons.reporteya.entity.Contact;
import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.ContactService;
import com.cons.reporteya.service.UserService;

@Controller
public class UserController {
	
	private final UserService userServ;
	private final ContactService contServ;
	public UserController(UserService uSer, ContactService cServ) {
		this.userServ = uSer;
		this.contServ = cServ;
	}
	
	@GetMapping("/profile")
	public String showProfile(@ModelAttribute("cont") Contact contact, Model model, Principal principal) {
		
		if(principal.getName()==null) {
			//return "redirect"
		}
		
		User us = userServ.findByEmail(principal.getName());
		model.addAttribute("user", userServ.findByEmail(principal.getName()));
		
		if (us.getContact() != null)
			model.addAttribute("contact", us.getContact());

		return "count/profile";
	}
	
	@PostMapping("/profile")
	public String formProfile(@ModelAttribute ("cont")Contact contact, Model model, Principal principal) {
		User us = userServ.findByEmail(principal.getName());
		
//		us.setContact(contact);
		
		contact.setUser(us);
		contServ.newContact(contact);
		
		return "redirect:/profile";
		
	}
	
	@PutMapping("/profile")
	public String editProfile(@ModelAttribute ("cont")Contact contact, Model model, Principal principal) {
		User us = userServ.findByEmail(principal.getName());

		us.getContact().setAddress(contact.getAddress());
		us.getContact().setNumber(contact.getNumber());
		us.getContact().setEmail(contact.getEmail());
		
		contServ.editContact(us.getContact());
		model.addAttribute("usuario",us);
		return "redirect:/profile";
	}
	

}
