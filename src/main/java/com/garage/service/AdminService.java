package com.garage.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage.authentication.JwtTokenHelper;
import com.garage.dto.AdminAllResponse;
import com.garage.dto.AdminResponse;
import com.garage.dto.CustomerResponse;
import com.garage.dto.MechanicResponse;
import com.garage.dto.Response;
import com.garage.dto.SupportResponse;
import com.garage.dto.TowTruckCompanyResponse;
import com.garage.entity.Customer;
import com.garage.entity.Mechanic;
import com.garage.entity.Role;
import com.garage.entity.SuperAdmin;
import com.garage.entity.Support;
import com.garage.entity.TowTruckCompany;
import com.garage.entity.User;
import com.garage.repository.CustomerRepository;
import com.garage.repository.MechanicRepository;
import com.garage.repository.RoleRepository;
import com.garage.repository.SuperAdminRepository;
import com.garage.repository.SupportRepository;
import com.garage.repository.TowTruckCompanyRepository;
import com.garage.repository.UserRepository;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Service
public class AdminService {
	@Autowired
	SuperAdminRepository adminRepository;
	@Autowired
	CustomerRepository customerRepository;
	AdminResponse adminResponse = new AdminResponse();
	AdminAllResponse adminAllResponse = new AdminAllResponse();
	// TowTruckCompanyResponse towTruckCompanyResponse=new
	// TowTruckCompanyResponse();
	@Autowired
	EmailService emailService;
	@Autowired
	MechanicRepository mechanicRepository;
	@Autowired
	TowTruckCompanyRepository towTruckRepository;
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	SupportRepository supportRepository;

	public ResponseEntity<AdminResponse> signUp(SuperAdmin admin) {

		admin.setFirstName(admin.getFirstName());
		admin.setLastName(admin.getLastName());
		admin.setEmail(admin.getEmail());
		admin.setRole("ADMIN");
		admin.setActive((byte) 1);
		admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));
		adminRepository.save(admin);
		return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminResponse> signIn(SuperAdmin admin) {

		SuperAdmin sup = adminRepository.findByEmail(admin.getEmail());
		if (sup == null) {
			adminResponse.setMessage(" not found");
			adminResponse.setCode(404);
			adminResponse.setToken(null);
			adminResponse.setError(HttpStatus.NOT_FOUND.name());
			adminResponse.setData(sup);
			return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.NOT_FOUND);
		}
		if (!sup.getRole().equals("ADMIN")) {
			adminResponse.setMessage("you are not admin");
			adminResponse.setData(null);
			adminResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminResponse.setCode(401);
			return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.UNAUTHORIZED);
		}

		try {
			this.authenticate(admin.getEmail().trim(), admin.getPassword());
		} catch (Exception e) {
			System.out.println(e);
			adminResponse.setMessage("Please try with correct password");
			adminResponse.setCode(401);
			adminResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminResponse.setToken(null);
			adminResponse.setData(null);
			return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.UNAUTHORIZED);
		}

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(admin.getEmail().trim());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		System.out.println(token);
		sup.setIsLoggedIn((byte) 1);
		// sup.setId(sup.getId());
		// admin.setId(sup.getId());
		admin.setPassword(sup.getPassword());
		adminRepository.save(sup);
		adminResponse.setMessage("sign-in successfully");
		adminResponse.setCode(200);
		adminResponse.setError(null);
		adminResponse.setToken(token);
		adminResponse.setData(sup);
		;
		return new ResponseEntity<AdminResponse>(adminResponse, HttpStatus.OK);
	}

	private void authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		this.authenticationManager.authenticate(authenticationToken);

	}

	public ResponseEntity<AdminAllResponse> findAllCustomers() {
		List<Customer> customers = customerRepository.findByActive((byte) 1);
		if (customers == null) {
			adminAllResponse.setMessage("no data found");
			adminAllResponse.setData(customers);
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);

			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<CustomerResponse> customerResponses = new ArrayList<CustomerResponse>();
		for (Customer c : customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setIsEmailVerified(c.getIsEmailVerified());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setModificationTimestamp(c.getModificationTimestamp());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setProfilePicturePath(c.getProfilePicturePath());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("customers!");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);

		adminAllResponse.setData(customerResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	// find unapproved Customers
	public ResponseEntity<AdminAllResponse> findAllUnapprovedCustomers() {
		List<Customer> customers = customerRepository.findByIsApproved((byte) 0);
		if (customers.isEmpty()) {
			adminAllResponse.setMessage("no customers found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(customers);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<CustomerResponse> customerResponses = new ArrayList<CustomerResponse>();
		for (Customer c : customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setNationalId(c.getNationalId());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setModificationTimestamp(c.getModificationTimestamp());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("unapproved customers!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(customerResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getUnVerifiedCustomers() {
		List<Customer> customers = customerRepository.findByIsEmailVerified((byte) 0);
		if (customers.isEmpty()) {
			adminAllResponse.setMessage("no customers found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(customers);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		List<CustomerResponse> customerResponses = new ArrayList<CustomerResponse>();
		for (Customer c : customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setIsEmailVerified(c.getIsEmailVerified());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setModificationTimestamp(c.getModificationTimestamp());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setProfilePicturePath(c.getProfilePicturePath());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("unverified customers!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(customerResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

//find verified customers
	public ResponseEntity<AdminAllResponse> findVerifiedCustomers() {
		List<Customer> customers = customerRepository.findByIsEmailVerified((byte) 1);
		if (customers.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setError(null);
			adminAllResponse.setData(customers);
			adminAllResponse.setCode(200);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<CustomerResponse> customerResponses = new ArrayList<CustomerResponse>();
		for (Customer c : customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setIsEmailVerified(c.getIsEmailVerified());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("verified customers!");
		adminAllResponse.setError(null);
		adminAllResponse.setData(customerResponses);
		adminAllResponse.setCode(200);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> approveCustomer(int customerId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		Customer customer = customerRepository.findByIdAndActive(customerId, (byte) 1);
		if (customer == null) {
			adminAllResponse.setMessage("customer not found!..");
			adminAllResponse.setData(customer);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setCode(400);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (customer.getIsEmailVerified() == 0) {
			adminAllResponse.setMessage("customers email is not verified");
			adminAllResponse.setData(customer);
			adminResponse.setError(HttpStatus.BAD_REQUEST.name());
			adminResponse.setCode(400);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.BAD_REQUEST);
		}
		if (customer.getIsApproved() == 1) {
			adminAllResponse.setMessage("customer already approved!..");
			adminAllResponse.setData(null);
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}

		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", customer.getEmail());
			// replacements.put("otpnum", String.valueOf(generateOTPNumber(email)));

			emailService.sendMail(customer.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate1(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}

		adminResponse.setCode(200);
		customer.setIsApproved((byte) 1);
		customer.setIsActive((byte) 1);
		customer.setStatus("APPROVED");
		customerRepository.save(customer);
		adminRepository.save(admin);
		adminAllResponse.setMessage("customer approved");
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getApprovedCustomers() {
		List<Customer> customers = customerRepository.findByIsApproved((byte) 1);
		if (customers.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(null);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<CustomerResponse> customerResponses = new ArrayList<CustomerResponse>();
		for (Customer c : customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setIsEmailVerified(c.getIsEmailVerified());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("approved customers!");
		adminAllResponse.setData(customerResponses);
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> deactivateCustomer(int customerId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		Customer customer = customerRepository.findByIdAndActive(customerId, (byte) 1);
		if (customer == null) {
			adminAllResponse.setMessage("customer not found!..");
			adminAllResponse.setData(customer);
			adminAllResponse.setCode(400);
			adminAllResponse.setError(HttpStatus.BAD_REQUEST.name());
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.BAD_REQUEST);
		}
		if (customer.getIsActive() == 1) {
			adminAllResponse.setMessage("customer already deactivated!..");
			adminAllResponse.setData(null);
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		customer.setIsActive((byte) 0);
		customerRepository.save(customer);
		adminRepository.save(admin);
		adminAllResponse.setMessage("customer deactivated successfully!..");
		adminAllResponse.setError(null);
		adminAllResponse.setCode(200);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getAllmechanics() {
		List<Mechanic> mechanics = mechanicRepository.findByActive((byte) 1);
		if (mechanics == null) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setData(mechanics);
			adminAllResponse.setError(null);
			adminAllResponse.setCode(200);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getUnapprovedMechanics() {
		List<Mechanic> mechanics = mechanicRepository.findByIsApproved((byte) 0);
		if (mechanics.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanics);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getApprovedMechanics() {
		List<Mechanic> mechanics = mechanicRepository.findByIsApproved((byte) 1);
		if (mechanics.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanics);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getVerifiedMechanics() {
		List<Mechanic> mechanics = mechanicRepository.findByIsEmailVerified((byte) 1);
		if (mechanics.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanics);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getUnverifiedMechanics() {
		List<Mechanic> mechanics = mechanicRepository.findByIsEmailVerified((byte) 0);
		if (mechanics.isEmpty()) {
			adminAllResponse.setMessage("no data found!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanics);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> approveMechanic(int mechanicId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		Mechanic mechanic = mechanicRepository.findByIdAndActive(mechanicId, (byte) 1);
		if (mechanic == null) {
			adminAllResponse.setMessage("mechanic not found!..");
			adminAllResponse.setData(admin);
			adminAllResponse.setError(HttpStatus.BAD_REQUEST.name());
			adminAllResponse.setCode(400);

			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.BAD_REQUEST);
		}
		if (mechanic.getIsEmailVerified() == 0) {
			adminAllResponse.setMessage("mechanics email not verified!..");
			adminAllResponse.setData(mechanic);
			adminAllResponse.setError(HttpStatus.BAD_REQUEST.name());
			adminAllResponse.setCode(400);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.BAD_REQUEST);
		}
		if (mechanic.getIsApproved() == 1) {
			adminAllResponse.setMessage("aleady approved!..");
			adminAllResponse.setData(null);
			adminAllResponse.setError(null);
			adminAllResponse.setCode(200);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", mechanic.getEmail());
			// replacements.put("otpnum", String.valueOf(generateOTPNumber(email)));

			emailService.sendMail(mechanic.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate1(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}
		mechanic.setIsApproved((byte) 1);
		mechanic.setIsActive((byte) 1);
		mechanicRepository.save(mechanic);
		adminRepository.save(admin);
		adminAllResponse.setMessage("mechanic successfully approved!");
		adminAllResponse.setData(null);
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> deactivateMechanic(int mechanicId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		Mechanic mechanic = mechanicRepository.findByIdAndActive(mechanicId, (byte) 1);
		if (mechanic == null) {

			adminAllResponse.setMessage("mechanic not found!..");
			adminAllResponse.setData(null);
			adminAllResponse.setError(HttpStatus.BAD_REQUEST.name());
			adminAllResponse.setCode(400);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.BAD_REQUEST);
		}
		if (mechanic.getIsActive() == 0) {
			adminAllResponse.setMessage("mechanic already deactivated!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		mechanic.setIsActive((byte) 0);
		mechanicRepository.save(mechanic);
		adminAllResponse.setMessage("mechanic successfully deactivated");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getAllMechanics() {
		Iterable<Mechanic> mechanics = mechanicRepository.findAll();
		if (mechanics == null) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanics);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<MechanicResponse> mechanicResponses = new ArrayList<MechanicResponse>();
		for (Mechanic m : mechanics) {
			MechanicResponse mechanicResponse = new MechanicResponse();
			mechanicResponse.setCityName(m.getCityName());
			mechanicResponse.setCreatedBy(m.getCreatedBy());
			mechanicResponse.setFirstName(m.getFirstName());
			mechanicResponse.setLastName(m.getLastName());
			mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
			mechanicResponse.setEmail(m.getEmail());
			mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
			mechanicResponse.setIdNumber(m.getIdNumber());
			mechanicResponse.setIsActive(m.getIsActive());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponse.setIsApproved(m.getIsApproved());
			mechanicResponse.setId(m.getId());
			mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
			mechanicResponse.setMobileNumber(m.getMobileNumber());
			mechanicResponse.setModifiedBy(m.getModifiedBy());
			mechanicResponse.setLatitude(m.getLatitude());
			mechanicResponse.setLongitude(m.getLongitude());
			mechanicResponse.setStatus(m.getStatus());
			mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
			mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
			mechanicResponse.setIsAvailable(m.getIsAvailable());
			mechanicResponse.setIsBooked(m.getIsBooked());
			mechanicResponse.setIsDone(m.getIsDone());
			mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
			mechanicResponses.add(mechanicResponse);
		}
		adminAllResponse.setMessage("mechanics!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(mechanicResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getAllTowTruckcompanies() {
		List<TowTruckCompany> towTruckCompany = towTruckRepository.findByIsActive((byte) 1);
		if (towTruckCompany == null) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(towTruckCompany);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<TowTruckCompanyResponse> towTruckCompanyResponses = new ArrayList<TowTruckCompanyResponse>();
		for (TowTruckCompany t : towTruckCompany) {
			TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
			towTruckCompanyResponse.setId(t.getId());
			towTruckCompanyResponse.setActive(t.getActive());
			towTruckCompanyResponse.setCityName(t.getCityName());
			towTruckCompanyResponse.setCompanyName(t.getCompanyName());
			towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
			towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
			towTruckCompanyResponse.setEmail(t.getEmail());
			towTruckCompanyResponse.setFirstName(t.getFirstName());
			towTruckCompanyResponse.setLastName(t.getLastName());
			towTruckCompanyResponse.setIsActive(t.getIsActive());
			towTruckCompanyResponse.setIsApproved(t.getIsApproved());
			towTruckCompanyResponse.setStatus(t.getStatus());
			towTruckCompanyResponse.setRole(t.getRole());
			towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
			towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
			towTruckCompanyResponse.setLatitude(t.getLatitude());
			towTruckCompanyResponse.setLongitude(t.getLongitude());
			towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
			towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
			towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
			towTruckCompanyResponses.add(towTruckCompanyResponse);
		}
		adminAllResponse.setMessage("towTruck companies!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(towTruckCompanyResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getUnverifiedTowTruckCompanies() {
		List<TowTruckCompany> towTruckCompany = towTruckRepository.findByIsEmailVerified((byte) 0);
		if (towTruckCompany.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<TowTruckCompanyResponse> towTruckCompanyResponses = new ArrayList<TowTruckCompanyResponse>();
		for (TowTruckCompany t : towTruckCompany) {
			TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
			towTruckCompanyResponse.setId(t.getId());
			towTruckCompanyResponse.setActive(t.getActive());
			towTruckCompanyResponse.setCityName(t.getCityName());
			towTruckCompanyResponse.setCompanyName(t.getCompanyName());
			towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
			towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
			towTruckCompanyResponse.setEmail(t.getEmail());
			towTruckCompanyResponse.setFirstName(t.getFirstName());
			towTruckCompanyResponse.setLastName(t.getLastName());
			towTruckCompanyResponse.setIsActive(t.getIsActive());
			towTruckCompanyResponse.setIsApproved(t.getIsApproved());
			towTruckCompanyResponse.setStatus(t.getStatus());
			towTruckCompanyResponse.setRole(t.getRole());
			towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
			towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
			towTruckCompanyResponse.setLatitude(t.getLatitude());
			towTruckCompanyResponse.setLongitude(t.getLongitude());
			towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
			towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
			towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
			towTruckCompanyResponses.add(towTruckCompanyResponse);
		}
		adminAllResponse.setMessage(" unverified towTruck companies!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(towTruckCompanyResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getVerifiedTowTruckCompanies() {
		List<TowTruckCompany> towTruckCompany = towTruckRepository.findByIsEmailVerified((byte) 1);
		if (towTruckCompany.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<TowTruckCompanyResponse> towTruckCompanyResponses = new ArrayList<TowTruckCompanyResponse>();
		for (TowTruckCompany t : towTruckCompany) {
			TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
			towTruckCompanyResponse.setId(t.getId());
			towTruckCompanyResponse.setActive(t.getActive());
			towTruckCompanyResponse.setCityName(t.getCityName());
			towTruckCompanyResponse.setCompanyName(t.getCompanyName());
			towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
			towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
			towTruckCompanyResponse.setEmail(t.getEmail());
			towTruckCompanyResponse.setFirstName(t.getFirstName());
			towTruckCompanyResponse.setLastName(t.getLastName());
			towTruckCompanyResponse.setIsActive(t.getIsActive());
			towTruckCompanyResponse.setIsApproved(t.getIsApproved());
			towTruckCompanyResponse.setStatus(t.getStatus());
			towTruckCompanyResponse.setRole(t.getRole());
			towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
			towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
			towTruckCompanyResponse.setLatitude(t.getLatitude());
			towTruckCompanyResponse.setLongitude(t.getLongitude());
			towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
			towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
			towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
			towTruckCompanyResponses.add(towTruckCompanyResponse);
		}
		adminAllResponse.setMessage("verified towTruck companies!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(towTruckCompanyResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);

	}

	public ResponseEntity<AdminAllResponse> getUnapprovedTowTruckCompanies() {
		List<TowTruckCompany> towTruckCompany = towTruckRepository.findByIsApproved((byte) 0);
		if (towTruckCompany.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<TowTruckCompanyResponse> towTruckCompanyResponses = new ArrayList<TowTruckCompanyResponse>();
		for (TowTruckCompany t : towTruckCompany) {
			TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
			towTruckCompanyResponse.setId(t.getId());
			towTruckCompanyResponse.setActive(t.getActive());
			towTruckCompanyResponse.setCityName(t.getCityName());
			towTruckCompanyResponse.setCompanyName(t.getCompanyName());
			towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
			towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
			towTruckCompanyResponse.setEmail(t.getEmail());
			towTruckCompanyResponse.setFirstName(t.getFirstName());
			towTruckCompanyResponse.setLastName(t.getLastName());
			towTruckCompanyResponse.setIsActive(t.getIsActive());
			towTruckCompanyResponse.setIsApproved(t.getIsApproved());
			towTruckCompanyResponse.setStatus(t.getStatus());
			towTruckCompanyResponse.setRole(t.getRole());
			towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
			towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
			towTruckCompanyResponse.setLatitude(t.getLatitude());
			towTruckCompanyResponse.setLongitude(t.getLongitude());
			towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
			towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
			towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
			towTruckCompanyResponses.add(towTruckCompanyResponse);
		}
		adminAllResponse.setMessage("unapproved towTruck companies!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(towTruckCompanyResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getApprovedTowTruckCompanies() {
		List<TowTruckCompany> towTruckCompany = towTruckRepository.findByIsApproved((byte) 1);
		if (towTruckCompany.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<TowTruckCompanyResponse> towTruckCompanyResponses = new ArrayList<TowTruckCompanyResponse>();
		for (TowTruckCompany t : towTruckCompany) {
			TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
			towTruckCompanyResponse.setId(t.getId());
			towTruckCompanyResponse.setActive(t.getActive());
			towTruckCompanyResponse.setCityName(t.getCityName());
			towTruckCompanyResponse.setCompanyName(t.getCompanyName());
			towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
			towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
			towTruckCompanyResponse.setEmail(t.getEmail());
			towTruckCompanyResponse.setFirstName(t.getFirstName());
			towTruckCompanyResponse.setLastName(t.getLastName());
			towTruckCompanyResponse.setIsActive(t.getIsActive());
			towTruckCompanyResponse.setIsApproved(t.getIsApproved());
			towTruckCompanyResponse.setStatus(t.getStatus());
			towTruckCompanyResponse.setRole(t.getRole());
			towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
			towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
			towTruckCompanyResponse.setLatitude(t.getLatitude());
			towTruckCompanyResponse.setLongitude(t.getLongitude());
			towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
			towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
			towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
			towTruckCompanyResponses.add(towTruckCompanyResponse);
		}
		adminAllResponse.setMessage("approved towTruck companies!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(towTruckCompanyResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> approveTowTruckCompany(int towTruckComapnyId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		TowTruckCompany towTruckCompany = towTruckRepository.findByIdAndActive(towTruckComapnyId, (byte) 1);
		if (towTruckCompany == null) {
			adminAllResponse.setMessage("towTruck not found!..");
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setCode(404);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (towTruckCompany.getIsApproved() == 1) {
			adminAllResponse.setMessage("towtruck already approved!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(null);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", towTruckCompany.getEmail());
			// replacements.put("otpnum", String.valueOf(generateOTPNumber(email)));

			emailService.sendMail(towTruckCompany.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate1(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}
		towTruckCompany.setIsActive((byte) 1);
		towTruckCompany.setIsApproved((byte) 1);
		towTruckRepository.save(towTruckCompany);
		adminAllResponse.setMessage("tow truck approved successfully!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> deactivateTowTruckCompany(int towTruckCompanyId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}

		TowTruckCompany towTruckCompany = towTruckRepository.findByIdAndActive(towTruckCompanyId, (byte) 1);
		if (towTruckCompany == null) {
			adminAllResponse.setMessage("towTruck not found!..");
			adminAllResponse.setData(towTruckCompany);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setCode(404);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (towTruckCompany.getIsActive() == 0) {
			adminAllResponse.setMessage("towtruck already deactivated!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(null);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		towTruckCompany.setIsActive((byte) 0);
		towTruckRepository.save(towTruckCompany);
		adminAllResponse.setMessage("towtruck deactivated successfully!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> registerMechanic(Mechanic mechanic) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}

		Mechanic mechanic1 = mechanicRepository.findByEmail(mechanic.getEmail());
		if (mechanic1 != null) {
			adminAllResponse.setMessage("mechanic already registered with this email!");
			adminAllResponse.setCode(409);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		Mechanic mechanic2 = mechanicRepository.findByMobileNumber(mechanic.getMobileNumber());
		if (mechanic2 != null) {
			adminAllResponse.setMessage("mechanic already registered with this mobileNumber!");
			adminAllResponse.setCode(409);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		String pass = mechanic.getPassword();

		mechanic.setActive((byte) 1);
		mechanic.setCityName((mechanic.getCityName()));
		mechanic.setCreatedBy(email);
		mechanic.setEmail(mechanic.getEmail());
		mechanic.setPassword(new BCryptPasswordEncoder().encode(mechanic.getPassword()));
		mechanic.setCreationTimestamp(new Timestamp(new Date().getTime()));
		mechanic.setIdNumber(null);
		mechanic.setIsActive((byte) 1);
		mechanic.setIsApproved((byte) 1);
		mechanic.setFirstName(mechanic.getFirstName());
		mechanic.setLastName(mechanic.getLastName());
		mechanic.setIsEmailVerified((byte) 1);
		mechanic.setMobileNumber(mechanic.getMobileNumber());
		mechanic.setStatus("APPROVED");
		mechanic.setRole("MECHANIC");
		mechanic.setMechanicStoreName(mechanic.getMechanicStoreName());
		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", mechanic.getEmail());
			replacements.put("password", pass);

			emailService.sendMail(mechanic.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate2(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}
		mechanicRepository.save(mechanic);
		adminAllResponse.setMessage("mechanic registered successfully!.");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> regiterTowTruckCompany(TowTruckCompany towTruckCompany) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		TowTruckCompany truck = towTruckRepository.findByEmail(towTruckCompany.getEmail());
		if (truck != null) {
			adminAllResponse.setMessage("already registered with this email");
			adminAllResponse.setCode(409);
			adminAllResponse.setData(null);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		TowTruckCompany truck2 = towTruckRepository.findByMobileNumber(towTruckCompany.getMobileNumber());
		if (truck2 != null) {
			adminAllResponse.setMessage("already registered with this mobileNumber");

			adminAllResponse.setCode(409);
			adminAllResponse.setData(null);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		String pass = towTruckCompany.getPassword();
		towTruckCompany.setEmail(towTruckCompany.getEmail());
		towTruckCompany.setActive((byte) 1);
		towTruckCompany.setIsActive((byte) 1);
		towTruckCompany.setIsEmailVerified((byte) 1);
		towTruckCompany.setIsApproved((byte) 1);
		towTruckCompany.setCompanyName(towTruckCompany.getCompanyName());
		towTruckCompany.setCityName(towTruckCompany.getCityName());
		towTruckCompany.setCreatedBy(email);
		towTruckCompany.setPassword(new BCryptPasswordEncoder().encode(towTruckCompany.getPassword()));
		towTruckCompany.setCreationTimestamp(new Timestamp(new Date().getTime()));
		towTruckCompany.setStatus("APPROVED");
		towTruckCompany.setRole("Tow_Truck");
		towTruckCompany.setFirstName(towTruckCompany.getFirstName());
		towTruckCompany.setLastName(towTruckCompany.getLastName());
		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", towTruckCompany.getEmail());
			replacements.put("password", pass);

			emailService.sendMail(towTruckCompany.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate2(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}
		towTruckRepository.save(towTruckCompany);
		adminAllResponse.setMessage("towTruck registered successfully!");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(null);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> registerSupportStaff(User user) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}

		User user1 = userRepository.findByEmail(user.getEmail());
		if (user1 != null) {
			adminAllResponse.setMessage("already registered with this email!");
			adminAllResponse.setCode(409);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		User user2 = userRepository.findByMobileNumber(user.getMobileNumber());
		if (user2 != null) {
			adminAllResponse.setMessage("already registered with this mobileNumber!");
			adminAllResponse.setCode(409);
			adminAllResponse.setError(HttpStatus.CONFLICT.name());
			adminAllResponse.setData(user1);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.CONFLICT);
		}
		Role role = roleRepository.findByName(user.getRole().getName());
		if (role == null) {
			adminAllResponse.setMessage("role not found!");
			adminAllResponse.setCode(404);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);

		}
		String pass = user.getPassword();
		user.setFirstName(user.getFirstName());
		user.setLastName(user.getLastName());
		int empId = getEmpId();
		user.setEmployeeId(empId);
		user.setIsActive((byte) 1);
		user.setActive((byte) 1);
		user.setRole(role);
		user.setIsEmailVerified((byte) 1);
		user.setAddress(user.getAddress());
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		user.setEmail(user.getEmail());
		user.setCreatedBy(email);
		user.setMobileNumber(user.getMobileNumber());
		user.setCreationTimestamp(new Timestamp(new Date().getTime()));
		try {
			Map<String, String> replacements = new HashMap<String, String>();
			replacements.put("user", user.getEmail());
			replacements.put("password", pass);

			emailService.sendMail(user.getEmail(), "Account Approved",
					new EmailTemplate("SendOtp.html").getTemplate2(replacements));
		} catch (Exception e) {

			e.printStackTrace();
			adminAllResponse.setCode(503);
			adminAllResponse.setMessage("email not sent");
			adminAllResponse.setError(e.getMessage());
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.SERVICE_UNAVAILABLE);
		}
		userRepository.save(user);
		adminAllResponse.setMessage("support staff registered successfully!");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public int getEmpId() {
		Random r = new Random(System.currentTimeMillis());
		System.out.println(1000000000 + r.nextInt(2000000000));
		return 10000 + r.nextInt(20000);
	}

	public ResponseEntity<AdminAllResponse> sinOut(int adminId) {
		SuperAdmin admin = adminRepository.findByIdAndActive(adminId, (byte) 1);
		if (admin == null) {
			adminAllResponse.setMessage("admin not found!");
			adminAllResponse.setData(admin);
			adminAllResponse.setCode(404);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());

			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		admin.setIsLoggedIn((byte) 0);
		admin.setLastloggedOutTimestamp(new Timestamp(new Date().getTime()));
		adminRepository.save(admin);
		adminAllResponse.setMessage("loggedout successfully!");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> deactivateSupportStaff(int userId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		User supportStaff = userRepository.findByIdAndActive(userId, (byte) 1);
		if (supportStaff == null) {
			adminAllResponse.setMessage("supportStaff not found");
			adminAllResponse.setCode(404);
			adminAllResponse.setData(supportStaff);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (supportStaff.getIsActive() == 0) {
			adminAllResponse.setMessage("already deactivated!");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		supportStaff.setIsActive((byte) 0);
		userRepository.save(supportStaff);
		adminAllResponse.setMessage("supportStaff deactivated successfully!");
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		adminAllResponse.setCode(200);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> activateSupportStaff(int userId) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}

		User user = userRepository.findByIdAndActive(userId, (byte) 1);
		if (user == null) {
			adminAllResponse.setMessage("user not found!");
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setCode(404);
			adminAllResponse.setData(user);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (user.getIsActive() == 1) {
			adminAllResponse.setMessage("already activated!");
			adminAllResponse.setError(null);
			adminAllResponse.setCode(200);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		user.setIsActive((byte) 1);
		userRepository.save(user);
		adminAllResponse.setMessage("support staff activated!");
		adminAllResponse.setError(null);
		adminAllResponse.setCode(200);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> getAllCustomerSupports() {
		List<Support> supports = supportRepository.findByActive((byte) 1);
		if (supports.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(supports);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<SupportResponse> supportResponses = new ArrayList<SupportResponse>();
		for (Support s : supports) {
			SupportResponse supportResponse = new SupportResponse();
			supportResponse.setId(s.getId());
			supportResponse.setCustomerId(s.getCustomer().getId());
			supportResponse.setStatus(s.getStatus());
			supportResponse.setCustomerName(s.getCustomer().getFirstName());
			supportResponse.setActive(s.getActive());
			supportResponse.setCrationtionTimestamp(s.getCreationTimestamp());
			supportResponse.setDetails(s.getDetails());
			supportResponse.setSupportNumber(s.getSupportNumber());
			supportResponse.setModificationTimestamp(s.getModificationTimestamp());
			supportResponse.setCreatedBY(s.getCreatedBy());
			supportResponse.setStatus(s.getStatus());
			supportResponse.setModifiedBy(s.getModifiedBY());
			supportResponses.add(supportResponse);
		}
		adminAllResponse.setMessage("all tickets!..");
		adminAllResponse.setCode(200);
		adminAllResponse.setData(supportResponses);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}
	public ResponseEntity<AdminAllResponse> getActiveCustomers(){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
		System.out.println(email);
		SuperAdmin admin = adminRepository.findByEmail(email);
		if (admin == null) {
			adminAllResponse.setMessage("invalid token");
			adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
			adminAllResponse.setCode(401);
			adminAllResponse.setData(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
		}
		List<Customer> customers=customerRepository.findByIsActive((byte)1);
		if(customers.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(customers);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse,HttpStatus.OK);
		}
		ArrayList<CustomerResponse> customerResponses=new ArrayList<CustomerResponse>();
		for(Customer c:customers) {
			CustomerResponse customerResponse = new CustomerResponse();
			customerResponse.setId(c.getId());
			customerResponse.setFirstName(c.getFirstName());
			customerResponse.setLastName(c.getLastName());
			customerResponse.setCityName(c.getCityName());
			customerResponse.setCreatedBy(c.getCreatedBy());
			customerResponse.setCreationTimestamp(c.getCreationTimestamp());
			customerResponse.setMobileNumber(c.getMobileNumber());
			customerResponse.setIsEmailVerified(c.getIsEmailVerified());
			customerResponse.setEmail(c.getEmail());
			customerResponse.setDrivingLicense(c.getDrivingLicense());
			customerResponse.setIsActive(c.getIsActive());
			customerResponse.setStatus(c.getStatus());
			customerResponse.setIsApproved(c.getIsApproved());
			customerResponse.setIsLoggedIn(c.getIsLoggedIn());
			customerResponse.setLatitude(c.getLatitude());
			customerResponse.setLongitude(c.getLongitude());
			customerResponses.add(customerResponse);
		}
		adminAllResponse.setMessage("active customers!");
		adminAllResponse.setData(customerResponses);
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
			
		}
	  public ResponseEntity<AdminAllResponse> getActiveMechanics(){
		  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
			System.out.println(email);
			SuperAdmin admin = adminRepository.findByEmail(email);
			if (admin == null) {
				adminAllResponse.setMessage("invalid token");
				adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
				adminAllResponse.setCode(401);
				adminAllResponse.setData(null);
				return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
			}
			List<Mechanic> mechanics=mechanicRepository.findByIsActive((byte)1);
			if(mechanics.isEmpty()) {
				adminAllResponse.setMessage("no data found!");
				adminAllResponse.setCode(200);
				adminAllResponse.setError(null);
				adminAllResponse.setData(mechanics);
				return new ResponseEntity<AdminAllResponse>(adminAllResponse,HttpStatus.OK);
			}
			ArrayList<MechanicResponse> mechanicResponses=new ArrayList<MechanicResponse>();
			for (Mechanic m : mechanics) {
				MechanicResponse mechanicResponse = new MechanicResponse();
				mechanicResponse.setCityName(m.getCityName());
				mechanicResponse.setCreatedBy(m.getCreatedBy());
				mechanicResponse.setFirstName(m.getFirstName());
				mechanicResponse.setLastName(m.getLastName());
				mechanicResponse.setIsLoggedIn(m.getIsLoggedIn());
				mechanicResponse.setEmail(m.getEmail());
				mechanicResponse.setCreationTimestamp(m.getCreationTimestamp());
				mechanicResponse.setIdNumber(m.getIdNumber());
				mechanicResponse.setIsActive(m.getIsActive());
				mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
				mechanicResponse.setIsApproved(m.getIsApproved());
				mechanicResponse.setId(m.getId());
				mechanicResponse.setModificationTimestamp(m.getModificationTimestamp());
				mechanicResponse.setMobileNumber(m.getMobileNumber());
				mechanicResponse.setModifiedBy(m.getModifiedBy());
				mechanicResponse.setLatitude(m.getLatitude());
				mechanicResponse.setLongitude(m.getLongitude());
				mechanicResponse.setStatus(m.getStatus());
				mechanicResponse.setProfilePicturePath(m.getProfilePicturePath());
				mechanicResponse.setMechanicStoreName(m.getMechanicStoreName());
				mechanicResponse.setIsAvailable(m.getIsAvailable());
				mechanicResponse.setIsBooked(m.getIsBooked());
				mechanicResponse.setIsDone(m.getIsDone());
				mechanicResponse.setIsEmailVerified(m.getIsEmailVerified());
				mechanicResponses.add(mechanicResponse);
			}
			adminAllResponse.setMessage("Active mechanics!..");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(mechanicResponses);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	  }
	  public ResponseEntity<AdminAllResponse> getActiveTowTruckCompanies(){
		  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			String email = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
			System.out.println(email);
			SuperAdmin admin = adminRepository.findByEmail(email);
			if (admin == null) {
				adminAllResponse.setMessage("invalid token");
				adminAllResponse.setError(HttpStatus.UNAUTHORIZED.name());
				adminAllResponse.setCode(401);
				adminAllResponse.setData(null);
				return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.UNAUTHORIZED);
			}
			List<TowTruckCompany> towTruckCompanies=towTruckRepository.findByIsActive((byte)1);
			if(towTruckCompanies.isEmpty()) {
				adminAllResponse.setMessage("no data found!");
				adminAllResponse.setCode(200);
				adminAllResponse.setError(null);
				adminAllResponse.setData(towTruckCompanies);
				return new ResponseEntity<AdminAllResponse>(adminAllResponse,HttpStatus.OK);
			}
			ArrayList<TowTruckCompanyResponse> truckCompanies=new ArrayList<TowTruckCompanyResponse>();
			for (TowTruckCompany t : towTruckCompanies) {
				TowTruckCompanyResponse towTruckCompanyResponse = new TowTruckCompanyResponse();
				towTruckCompanyResponse.setId(t.getId());
				towTruckCompanyResponse.setActive(t.getActive());
				towTruckCompanyResponse.setCityName(t.getCityName());
				towTruckCompanyResponse.setCompanyName(t.getCompanyName());
				towTruckCompanyResponse.setCreatedBy(t.getCreatedBy());
				towTruckCompanyResponse.setCreationTimestamp(t.getCreationTimestamp());
				towTruckCompanyResponse.setEmail(t.getEmail());
				towTruckCompanyResponse.setFirstName(t.getFirstName());
				towTruckCompanyResponse.setLastName(t.getLastName());
				towTruckCompanyResponse.setIsActive(t.getIsActive());
				towTruckCompanyResponse.setIsApproved(t.getIsApproved());
				towTruckCompanyResponse.setStatus(t.getStatus());
				towTruckCompanyResponse.setRole(t.getRole());
				towTruckCompanyResponse.setRegistrationNumber(t.getRegistrationNumber());
				towTruckCompanyResponse.setProfilePicturePath(t.getProfilePicturePath());
				towTruckCompanyResponse.setLatitude(t.getLatitude());
				towTruckCompanyResponse.setLongitude(t.getLongitude());
				towTruckCompanyResponse.setMobileNumber(t.getMobileNumber());
				towTruckCompanyResponse.setModificationTimestamp(t.getModificationTimestamp());
				towTruckCompanyResponse.setIsEmailVerified(t.getIsEmailVerified());
				truckCompanies.add(towTruckCompanyResponse);
			}
			adminAllResponse.setMessage("active towTruck companies!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(truckCompanies);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	  }
	}
