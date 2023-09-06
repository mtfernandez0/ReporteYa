package com.cons.reporteya.service;

import org.springframework.stereotype.Service;

import com.cons.reporteya.entity.Contact;
import com.cons.reporteya.repository.ContactRepository;

@Service
public class ContactService {

	private final ContactRepository contactRepository;

	public ContactService(ContactRepository cRepo) {

		this.contactRepository = cRepo;
	}
	
	 public Contact newContact(Contact number) {
	        return contactRepository.save(number);
	    }

}
