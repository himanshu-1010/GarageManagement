package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.Response;
import com.garage.entity.VehicleType;
import com.garage.service.VehicleService;

@RestController
@RequestMapping("vehicle")
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;
	
	@RequestMapping(value = "addType", method = RequestMethod.POST)
	public ResponseEntity<Response> addType(@RequestBody VehicleType vehicleType) {
		return vehicleService.addVehicleType(vehicleType);
	}
	@RequestMapping(value = "addVehicle", method = RequestMethod.POST)
	public ResponseEntity<Response> addVehicle(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "vehicleTypeId", required = true) int vehicleTypeId) {
		return vehicleService.addVehicle(name, vehicleTypeId);
	}
}
