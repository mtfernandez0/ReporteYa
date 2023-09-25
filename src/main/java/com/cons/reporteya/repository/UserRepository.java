package com.cons.reporteya.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.cons.reporteya.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
