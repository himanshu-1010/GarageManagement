package com.garage.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.garage.dto.Response;
import com.garage.entity.Vehicle;
import com.garage.entity.VehicleType;
import com.garage.repository.VehicleRepository;
import com.garage.repository.VehicleTypeRepository;

@Service
public class VehicleService {

	private Response response = new Response();
	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	public ResponseEntity<Response> addVehicleType(VehicleType vehicleType) {

		vehicleType.setActive((byte) 1);
		vehicleType.setCreationTimestamp(new Timestamp(new Date().getTime()));
//		vehicleType.setCreatedBy(vehicleType.getEmail());
		vehicleType.setModificationTimestamp(new Timestamp(new Date().getTime()));
//		vehicleType.setModifiedBy(vehicleType.getEmail());
		vehicleTypeRepository.save(vehicleType);

		Iterable<VehicleType> vehicleType2 = vehicleTypeRepository.findAll();

		response.setMessage("success");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(vehicleType2);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response> addVehicle(String name, int vehicleTypeId) {
		
		VehicleType type = vehicleTypeRepository.findByIdAndActive(vehicleTypeId, (byte)1);
		if(type==null) {
			response.setMessage("vehicle type not found");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}
		System.out.println(type.getName());
		Vehicle vehicle = new Vehicle();
		vehicle.setName(name);
		vehicle.setVehicleType(type);
		vehicle.setActive((byte)1);
		vehicle.setCreationTimestamp(new Timestamp(new Date().getTime()));
//		vehicleType.setCreatedBy(vehicleType.getEmail());
		vehicle.setModificationTimestamp(new Timestamp(new Date().getTime()));
//		vehicleType.setModifiedBy(vehicleType.getEmail());
		vehicleRepository.save(vehicle);
		
		List<Vehicle> VehicleList = vehicleRepository.findByVehicleTypeIdAndActive(type.getId(), (byte)1);
		
		response.setMessage("success");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(VehicleList);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
