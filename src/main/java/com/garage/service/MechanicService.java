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
import com.garage.entity.Mechanic;
import com.garage.repository.MechanicRepository;

@Service
public class MechanicService {

	private Response response = new Response();
	@Autowired
	private MechanicRepository mechanicRepository;
	@Autowired
	private OTPService otpService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public ResponseEntity<Response> generateOtpForSignUp(String email) {

		Mechanic mechanic = mechanicRepository.findByEmail(email);
		if (mechanic != null) {
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

	public ResponseEntity<Response> signUp(Mechanic mechanic) {

		if (mechanic.getEmail() == null) {
			response.setMessage("email is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getMobileNumber() == null) {
			response.setMessage("mobileNumber is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getCityName() == null) {
			response.setMessage("cityName is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getPassword() == null) {
			response.setMessage("password is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getOtp() == null) {
			response.setMessage("otp is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		// empty fields checks
		if (mechanic.getEmail() == "" || mechanic.getEmail().contains(" ")) {
			response.setMessage("Invalid Email");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getMobileNumber() == "" || mechanic.getMobileNumber().contains(" ")) {
			response.setMessage("Invalid Mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getCityName() == "") {
			response.setMessage("Invalid City name");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getPassword() == "") {
			response.setMessage("Invalid Password");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (mechanic.getOtp() == "") {
			response.setMessage("Invalid OTP!");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		Mechanic getMechanic = mechanicRepository.findByEmail(mechanic.getEmail());

		boolean mobile = mechanic.getMobileNumber().matches("^[a-zA-Z]*$");
		if (mobile == true) {
			response.setMessage("Invalid Mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (getMechanic != null) {
			response.setMessage("email is alrady registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		Mechanic mobileValidation = mechanicRepository.findByMobileNumber(mechanic.getMobileNumber());
		if (mobileValidation != null) {
			response.setMessage("mobile number is already registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		// verifying OTP
		response = verifyOTP(mechanic);

		if (response.getMessage().equals("Invalid OTP!")) {
			response.setMessage(response.getMessage());
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		mechanic.setCreationTimestamp(new Timestamp(new Date().getTime()));
		mechanic.setCreatedBy(mechanic.getEmail());
		mechanic.setModificationTimestamp(new Timestamp(new Date().getTime()));
		mechanic.setModifiedBy(mechanic.getEmail());
		mechanic.setPassword(new BCryptPasswordEncoder().encode(mechanic.getPassword()));
		mechanic.setRole("MECHANIC");
		mechanic.setActive((byte) 1);
		mechanic.setStatus("PENDING");
		mechanic.setIsEmailVerified((byte) 1);

		mechanicRepository.save(mechanic);

		response.setMessage("signup successfully, admin will approve you soon");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	// verify OTP
	public Response verifyOTP(Mechanic mechanic) {

		if (otpService.validateOTP(mechanic.getEmail().trim(), Integer.parseInt(mechanic.getOtp().trim()))
				.equals("FAIL")) {

			response.setMessage("Invalid OTP!");
			response.setCode(404);
			return response;
		}

		response.setMessage("OTP Verified!");
		response.setCode(200);
		return response;
	}
	
	public ResponseEntity<Response> signIn(Mechanic mechanic) {

		Mechanic getMechanic = mechanicRepository.findByEmail(mechanic.getEmail().trim());
		if (getMechanic == null) {
			response.setMessage("mechanic not found with this email");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(getMechanic);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}

		if (getMechanic.getIsApproved() == 0) {
			response.setMessage("you are not approved by admin yet!");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
		}
		
		if(getMechanic.getStatus().equalsIgnoreCase("REJECTED")) {
			response.setMessage("Your account is rejected");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		this.authenticate(mechanic.getEmail().trim(), mechanic.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(mechanic.getEmail().trim());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		System.out.println(token);

		response.setMessage("sign-in successfully");
		response.setCode(200);
		response.setError(null);
		response.setToken(token);
		response.setResult(getMechanic);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	private void authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		this.authenticationManager.authenticate(authenticationToken);

	}

	public ResponseEntity<Response> completeProfile(int id, String firstName, String lastName, String email,
			String mobileNumber, String password, String cityName, String idNumber, String mechanicStoreName,
			String lat, String lng) {

		Mechanic getMechanic = mechanicRepository.findByIdAndActive(id, (byte) 1);
		if (getMechanic == null) {
			response.setMessage("mechanic not found");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(getMechanic);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}

		if (firstName != null)
			if (!firstName.equals(""))
				getMechanic.setFirstName(firstName);
		if (lastName != null)
			if (!lastName.equals(""))
				getMechanic.setLastName(lastName);
		if (email != null)
			if (!email.equalsIgnoreCase(""))
				getMechanic.setEmail(email);
		if (mobileNumber != null)
			if (!mobileNumber.equals(""))
				getMechanic.setMobileNumber(mobileNumber);
		if (password != null)
			if (!password.equals(""))
				getMechanic.setPassword(password); // (new BCryptPasswordEncoder().encode(password.trim()));
		if (cityName != null)
			if (!cityName.equals(""))
				getMechanic.setCityName(cityName);
		if (idNumber != null)
			if (!idNumber.equals(""))
				getMechanic.setIdNumber(idNumber);
		if (mechanicStoreName != null)
			if (!mechanicStoreName.equals(""))
				getMechanic.setMechanicStoreName(mechanicStoreName);
		if (lat != null)
			if (!lat.equals(""))
				getMechanic.setMechanicStoreName(lat);
		if (lng != null)
			if (!lng.equals(""))
				getMechanic.setMechanicStoreName(lng);

		getMechanic.setModificationTimestamp(new Timestamp(new Date().getTime()));
		getMechanic.setModifiedBy(getMechanic.getEmail());

		mechanicRepository.save(getMechanic);
		response.setMessage("success");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(getMechanic);

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
