package com.garage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.garage.entity.Mechanic;

public interface MechanicRepository extends CrudRepository<Mechanic, Integer>{
	
	Mechanic findByIdAndActive(int id, byte active);
	Mechanic findByEmail(String email);
	Mechanic findByMobileNumber(String number);
	Optional<Mechanic> findByEmailAndActive(String email, byte a);
	List<Mechanic> findByIsApproved(byte isApproved);
	List<Mechanic> findByIsEmailVerified(byte isEmailVerified);
	List<Mechanic> findByActive(byte active);
	List<Mechanic> findByIsActive(byte isActive);
}
