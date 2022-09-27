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
import com.garage.dto.CustomerResponse;
import com.garage.dto.Response;
import com.garage.entity.Customer;
import com.garage.repository.CustomerRepository;

@Service
public class CustomerService {

	private Response response = new Response();
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private OTPService otpService;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	CustomerResponse customerResponse = new CustomerResponse();

	public ResponseEntity<Response> generateOtpForSignUp(String email) {

		Customer customer = customerRepository.findByEmail(email);
		if (customer != null) {
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

	public ResponseEntity<Response> signUp(Customer customer) {

		if (customer.getEmail() == null) {
			response.setMessage("email is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getMobileNumber() == null) {
			response.setMessage("mobileNumber is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getCityName() == null) {
			response.setMessage("cityName is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getPassword() == null) {
			response.setMessage("password is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getOtp() == null) {
			response.setMessage("otp is missing");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		// empty fields checks
		if (customer.getEmail() == "" || customer.getEmail().contains(" ")) {
			response.setMessage("Invalid Email");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getMobileNumber() == "" || customer.getMobileNumber().contains(" ")) {
			response.setMessage("Invalid Mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getCityName() == "") {
			response.setMessage("Invalid City name");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getPassword() == "") {
			response.setMessage("Invalid Password");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (customer.getOtp() == "") {
			response.setMessage("Invalid OTP!");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		Customer getCustomer = customerRepository.findByEmail(customer.getEmail().trim());

		boolean mobile = customer.getMobileNumber().matches("^[a-zA-Z]*$");
		if (mobile == true) {
			response.setMessage("Invalid Mobile number");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		if (getCustomer != null) {
			response.setMessage("email is alrady registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		Customer mobileValidation = customerRepository.findByMobileNumber(customer.getMobileNumber());
		if (mobileValidation != null) {
			response.setMessage("mobile number is already registered");
			response.setCode(409);
			response.setError(HttpStatus.CONFLICT.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.CONFLICT);
		}

		// verifying OTP
		response = verifyOTP(customer);

		if (response.getMessage().equals("Invalid OTP!")) {
			response.setMessage(response.getMessage());
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		customer.setCreationTimestamp(new Timestamp(new Date().getTime()));
		customer.setCreatedBy(customer.getEmail());
		customer.setModificationTimestamp(new Timestamp(new Date().getTime()));
		customer.setModifiedBy(customer.getEmail());
		customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
		customer.setRole("CUSTOMER");
		customer.setActive((byte) 1);
		customer.setStatus("APPROVED");
		customer.setIsApproved((byte) 1);
		customer.setIsEmailVerified((byte) 1);

		customerRepository.save(customer);

		response.setMessage("signup successfully");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response> signIn(Customer customer) {

		Customer getCustomer = customerRepository.findByEmail(customer.getEmail().trim());
		if (getCustomer == null) {
			response.setMessage("customer not found with this email");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(getCustomer);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}

		if (getCustomer.getIsApproved() == 0) {
			response.setMessage("you are not approved by admin yet!");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
		}

		if (getCustomer.getStatus().equalsIgnoreCase("REJECTED")) {
			response.setMessage("Your account is rejected");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			this.authenticate(customer.getEmail().trim(), customer.getPassword());
		} catch (Exception e) {
			System.out.println(e);
			response.setMessage("Please try with correct password");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
		}

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(customer.getEmail().trim());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		System.out.println(token);

		response.setMessage("sign-in successfully");
		response.setCode(200);
		response.setError(null);
		response.setToken(token);
		response.setResult(getCustomer);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private void authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		this.authenticationManager.authenticate(authenticationToken);

	}

	// verify OTP
	public Response verifyOTP(Customer customer) {

		if (otpService.validateOTP(customer.getEmail().trim(), Integer.parseInt(customer.getOtp().trim()))
				.equals("FAIL")) {

			response.setMessage("Invalid OTP!");
			response.setCode(404);
			return response;
		}

		response.setMessage("OTP Verified!");
		response.setCode(200);
		return response;
	}

	public ResponseEntity<Response> completeProfile(int id, String firstName, String lastName, String email,
			String mobileNumber, String password, String cityName, String nationalId, String drivingLicense) {

		Customer getCustomer = customerRepository.findByIdAndActive(id, (byte) 1);
		if (getCustomer == null) {
			response.setMessage("customer not found");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setToken(null);
			response.setResult(getCustomer);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}

		if (firstName != null)
			if (!firstName.equals(""))
				getCustomer.setFirstName(firstName);
		if (lastName != null)
			if (!lastName.equals(""))
				getCustomer.setLastName(lastName);
		if (email != null)
			if (!email.equalsIgnoreCase(""))
				getCustomer.setEmail(email);
		if (mobileNumber != null)
			if (!mobileNumber.equals(""))
				getCustomer.setMobileNumber(mobileNumber);
		if (password != null)
			if (!password.equals(""))
				getCustomer.setPassword(password); // (new BCryptPasswordEncoder().encode(password.trim()));
		if (cityName != null)
			if (!cityName.equals(""))
				getCustomer.setCityName(cityName);
		if (nationalId != null)
			if (!nationalId.equals(""))
				getCustomer.setNationalId(nationalId);
		if (drivingLicense != null)
			if (!drivingLicense.equals(""))
				getCustomer.setDrivingLicense(drivingLicense);

		getCustomer.setModificationTimestamp(new Timestamp(new Date().getTime()));
		getCustomer.setModifiedBy(getCustomer.getEmail());

		customerRepository.save(getCustomer);
		response.setMessage("success");
		response.setCode(200);
		response.setError(null);
		response.setToken(null);
		response.setResult(getCustomer);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public ResponseEntity<Response> findCustomerById(int customerId) {
		Customer customer = customerRepository.findByIdAndActive(customerId, (byte) 1);
		if (customer == null) {
			response.setCode(404);
			response.setToken(null);
			response.setMessage("customer not found!");
			response.setResult(customer);
			response.setError(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);

		}
		try {
			getCustomerResponse(customer);
		} catch (Exception e) {
			System.out.println(e);
			response.setMessage("Something went wrong!");
			response.setCode(400);
			response.setError(HttpStatus.BAD_REQUEST.name());
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("customer's details!");
		response.setCode(200);
		response.setToken(null);
		response.setError(null);
		response.setResult(customerResponse);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	public Customer getCustomerResponse(Customer getCustomer) {

		customerResponse.setId(getCustomer.getId());
		customerResponse.setFirstName(getCustomer.getFirstName());
		customerResponse.setLastName(getCustomer.getLastName());
		customerResponse.setCreationTimestamp(getCustomer.getCreationTimestamp());
		customerResponse.setCreatedBy(getCustomer.getCreatedBy());
		customerResponse.setModificationTimestamp(getCustomer.getModificationTimestamp());
		customerResponse.setModifiedBy(getCustomer.getModifiedBy());
		customerResponse.setEmail(getCustomer.getEmail());
		customerResponse.setMobileNumber(getCustomer.getMobileNumber());
		customerResponse.setCityName(getCustomer.getCityName());
		customerResponse.setNationalId(getCustomer.getNationalId());
		customerResponse.setDrivingLicense(getCustomer.getDrivingLicense());
		customerResponse.setIsApproved(getCustomer.getIsApproved());
		customerResponse.setIsEmailVerified(getCustomer.getIsEmailVerified());
		customerResponse.setProfilePicturePath(getCustomer.getProfilePicturePath());
		customerResponse.setStatus(getCustomer.getStatus());
		customerResponse.setLatitude(getCustomer.getLatitude());
		customerResponse.setLongitude(getCustomer.getLongitude());
		customerResponse.setIsLoggedIn(getCustomer.getIsLoggedIn());
		customerResponse.setIsActive(getCustomer.getIsActive());

		return getCustomer;
	}

}
