package com.garage.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.garage.entity.TowTruckCompany;


public interface TowTruckCompanyRepository extends CrudRepository<TowTruckCompany, Integer>{
	TowTruckCompany findByEmail(String email);
	TowTruckCompany findByMobileNumber(String mobile);
	TowTruckCompany findByIdAndActive(int id,byte active);
	Optional<TowTruckCompany> findByEmailAndActive(String email, byte a);
	List<TowTruckCompany> findByIsApproved(byte isApproved);
	List<TowTruckCompany> findByIsEmailVerified(byte isEmailVerified);
	List<TowTruckCompany> findByIsActive(byte isActive);
}
