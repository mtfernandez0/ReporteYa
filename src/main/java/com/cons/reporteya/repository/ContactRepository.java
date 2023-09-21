package com.cons.reporteya.repository;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.Contact;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

}
