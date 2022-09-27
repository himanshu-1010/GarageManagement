package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.Response;
import com.garage.entity.TowTruckCompany;
import com.garage.service.TowTruckCompanyService;

@RestController
@RequestMapping("towtruck")
public class TowTruckCompanyController {
	
	@Autowired
	private TowTruckCompanyService towTruckService;
	
	@RequestMapping(value = "/generateOtpForSignUp/{email}", method = RequestMethod.GET)
	public ResponseEntity<Response> generateOtpForSignUp(@PathVariable String email) {
		return towTruckService.generateOtpForSignUp(email);
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Response> signUp(@RequestBody TowTruckCompany towTruck) {
		return towTruckService.signUp(towTruck);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/signin")
	public ResponseEntity<Response> signIn(@RequestBody TowTruckCompany towTruck) {
		return towTruckService.signIn(towTruck);
	}
}
