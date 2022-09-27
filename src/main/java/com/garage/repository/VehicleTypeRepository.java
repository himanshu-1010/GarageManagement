package com.garage.repository;

import org.springframework.data.repository.CrudRepository;

import com.garage.entity.VehicleType;

public interface VehicleTypeRepository extends CrudRepository<VehicleType, Integer>{
	
	VehicleType findByIdAndActive(int id, byte ative);
}
