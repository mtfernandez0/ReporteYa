package com.cons.reporteya.service;

import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Contact;
import com.cons.reporteya.repository.ContactRepository;

@Service
public class ContactService {
	
	private final ContactRepository contactRepository;
		
	public ContactService(ContactRepository contactRepository) {
	
	this.contactRepository = contactRepository;
	}
	
	public Contact newContact(Contact cont) {
		return contactRepository.save(cont);
	}
	
	public Contact editContact(Contact cont) {
		return contactRepository.save(cont);
	}

}
