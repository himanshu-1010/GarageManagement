package com.garage.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.garage.entity.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer>{

	List<Vehicle> findByVehicleTypeIdAndActive(int id, byte active);
}
