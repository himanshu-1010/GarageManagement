package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.Response;
import com.garage.entity.Customer;
import com.garage.service.CustomerService;

@RestController
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/generateOtpForSignUp/{email}", method = RequestMethod.GET)
	public ResponseEntity<Response> generateOtpForSignUp(@PathVariable String email) {
		return customerService.generateOtpForSignUp(email);
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Response> signUp(@RequestBody Customer customer) {
		return customerService.signUp(customer);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/verifyOTP")
	public Response verifyOTP(@RequestBody Customer customer) {

		return customerService.verifyOTP(customer);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/signin")
	public ResponseEntity<Response> signIn(@RequestBody Customer customer) {
		return customerService.signIn(customer);
	}

	@RequestMapping(value = "/completeProfile", method = RequestMethod.POST)
	public ResponseEntity<Response> completeProfile(@RequestParam(value = "id", required = true) int id,
			@RequestParam(value = "firstName", required = false) String firstName,
			@RequestParam(value = "lastName", required = false) String lastName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "cityName", required = false) String cityName,
			@RequestParam(value = "nationalId", required = false) String nationalId,
			@RequestParam(value = "drivingLicense", required = false) String drivingLicense) {
		return customerService.completeProfile(id, firstName, lastName, email, mobileNumber, password, cityName,
				nationalId, drivingLicense);
	}

	@GetMapping("/getCustomer/{customerId}")
	private ResponseEntity<Response> getCustomerById(@PathVariable int customerId) {
		return customerService.findCustomerById(customerId);
	}
}
