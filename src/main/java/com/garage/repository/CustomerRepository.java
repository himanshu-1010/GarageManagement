package com.garage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.garage.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	
	Customer findByEmail(String email);
	Optional<Customer> findByEmailAndActive(String email, byte a);
	Customer findByMobileNumber(String mobile);
	Customer findByIdAndActive(int id, byte active);
	List<Customer> findByIsEmailVerified(byte isEmailVerified);
	List<Customer> findByIsApproved(byte isApproved);
	List<Customer> findByActive(byte active);
	List<Customer> findByIsActive(byte isActive);
}
