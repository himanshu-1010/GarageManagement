package com.garage.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage.authentication.JwtTokenHelper;
import com.garage.dto.Response;
import com.garage.entity.TowTruckCompany;
import com.garage.repository.TowTruckCompanyRepository;

@Service
public class TowTruckCompanyService {

	private Response response = new Response();
	@Autowired
	private TowTruckCompanyRepository towTruckRepository;
	@Autowired
	private OTPService otpService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public ResponseEntity<Response> generateOtpForSignUp(String email) {

		TowTruckCompany towTruck = towTruckRepository.findByEmail(email);
		if (towTruck != null) {
			response.setMessage("email is already registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}
		try {
			otpService.generateOTP(email);
		} catch (Exception e) {
			System.out.println(e);
			response.setMessage("OTP NOT GENERATED");
			response.setCode(400);
			response.setError(e.getMessage());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		response.setMessage("otp generated");
		response.setCode(200);
		response.setResult(null);
		response.setError(null);
		response.setToken(null);
		return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<Response> signUp(TowTruckCompany towTruck) {

		if (towTruck.getEmail() == null) {
			response.setMessage("email is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getMobileNumber() == null) {
			response.setMessage("mobileNumber is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getCityName() == null) {
			response.setMessage("city is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getCompanyName() == null) {
			response.setMessage("companyName is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getRegistrationNumber() == null) {
			response.setMessage("registrationNumber is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		// empty fields checks
		if (towTruck.getEmail() == "" || towTruck.getEmail().contains(" ")) {
			response.setMessage("Invalid Email");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getMobileNumber() == "" || towTruck.getMobileNumber().contains(" ")) {
			response.setMessage("Invalid mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getCityName() == "") {
			response.setMessage("Invalid city name");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getCompanyName() == "") {
			response.setMessage("Invalid company name");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		if (towTruck.getRegistrationNumber() == "") {
			response.setMessage("Invalid registration number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (towTruck.getPassword() == "") {
			response.setMessage("Invalid Password");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (towTruck.getOtp() == "") {
			response.setMessage("Invalid OTP!");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		TowTruckCompany getTowTruck = towTruckRepository.findByEmail(towTruck.getEmail().trim());

		boolean mobile = towTruck.getMobileNumber().matches("^[a-zA-Z]*$");
		if (mobile == true) {
			response.setMessage("Invalid Mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (getTowTruck != null) {
			response.setMessage("email is already registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		TowTruckCompany mobileValidation = towTruckRepository.findByMobileNumber(towTruck.getMobileNumber());
		if (mobileValidation != null) {
			response.setMessage("mobile number is already registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		// verifying OTP
		response = verifyOTP(towTruck);
		
		if (response.getMessage().equals("Invalid OTP!")) {
			response.setMessage(response.getMessage());
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		towTruck.setCreationTimestamp(new Timestamp(new Date().getTime()));
		towTruck.setCreatedBy(towTruck.getEmail());
		towTruck.setModificationTimestamp(new Timestamp(new Date().getTime()));
		towTruck.setModifiedBy(towTruck.getEmail());
		towTruck.setPassword(new BCryptPasswordEncoder().encode(towTruck.getPassword()));
		towTruck.setRole("TOW_TRUCK");
		towTruck.setActive((byte) 1);
		towTruck.setStatus("PENDING");
		towTruck.setIsEmailVerified((byte)1);

		towTruckRepository.save(towTruck);

		response.setMessage("signup successfully, admin will approve you soon");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<Response> signIn(TowTruckCompany towTruck) {

		TowTruckCompany getTowTruck = towTruckRepository.findByEmail(towTruck.getEmail().trim());
		if (getTowTruck == null) {
			response.setMessage("account not found with this email");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(getTowTruck);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}

		if (getTowTruck.getIsApproved() == 0) {
			response.setMessage("you are not approved by admin yet!");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
		}
		
		if(getTowTruck.getStatus().equalsIgnoreCase("REJECTED")) {
			response.setMessage("Your account is rejected");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		this.authenticate(towTruck.getEmail().trim(), towTruck.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(towTruck.getEmail().trim());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		System.out.println(token);

		response.setMessage("sign-in successfully");
		response.setCode(200);
		response.setError(null);
		response.setToken(token);
		response.setResult(getTowTruck);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		this.authenticationManager.authenticate(authenticationToken);

	}

	// verify OTP
	public Response verifyOTP(TowTruckCompany towTruck) {

		if (otpService.validateOTP(towTruck.getEmail().trim(), Integer.parseInt(towTruck.getOtp().trim()))
				.equals("FAIL")) {

			response.setMessage("Invalid OTP!");
			response.setCode(404);
			return response;
		}

		response.setMessage("OTP Verified!");
		response.setCode(200);
		return response;
	}
}
