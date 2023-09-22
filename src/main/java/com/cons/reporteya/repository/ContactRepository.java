package com.cons.reporteya.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cons.reporteya.entity.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

}
