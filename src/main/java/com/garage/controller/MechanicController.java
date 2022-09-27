package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.Response;
import com.garage.entity.Mechanic;
import com.garage.service.MechanicService;

@RestController
@RequestMapping("mechanic")
public class MechanicController {

	@Autowired
	MechanicService mechanicService;
	
	@RequestMapping(value = "/generateOtpForSignUp/{email}", method = RequestMethod.GET)
	public ResponseEntity<Response> generateOtpForSignUp(@PathVariable String email) {
		return mechanicService.generateOtpForSignUp(email);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Response> signUp(@RequestBody Mechanic mechanic) {
		return mechanicService.signUp(mechanic);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyOTP")
	public Response verifyOTP(@RequestBody Mechanic mechanic) {

		return mechanicService.verifyOTP(mechanic);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/signin")
	public ResponseEntity<Response> signIn(@RequestBody Mechanic mechanic) {
		return mechanicService.signIn(mechanic);
	}

	@RequestMapping(value = "/completeProfile", method = RequestMethod.POST)
	public ResponseEntity<Response> completeProfile(@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "cityName", required = false) String cityName,
			@RequestParam(value = "idNumber", required = false) String idNumber,
			@RequestParam(value = "mechanicStoreName", required = false) String mechanicStoreName,
			@RequestParam(value = "lat", required = false) String lat,
			@RequestParam(value = "lng", required = false) String lng) {
		return mechanicService.completeProfile(id, firstName, lastName, email, mobileNumber, password, cityName,
				idNumber, mechanicStoreName, lat, lng);
	}

}
