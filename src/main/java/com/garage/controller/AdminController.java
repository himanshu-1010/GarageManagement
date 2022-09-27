package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.AdminAllResponse;
import com.garage.dto.AdminResponse;
import com.garage.dto.Response;
import com.garage.entity.Mechanic;
import com.garage.entity.SuperAdmin;
import com.garage.entity.TowTruckCompany;
import com.garage.entity.User;
import com.garage.service.AdminService;

@RestController
@RequestMapping("admin")
public class AdminController {
	@Autowired
	AdminService adminService;

	@PostMapping("/signUp")
	public ResponseEntity<AdminResponse> signUp(@RequestBody SuperAdmin admin) {
		return adminService.signUp(admin);
	}

	@PostMapping("/signIn")
	public ResponseEntity<AdminResponse> signIn(@RequestBody SuperAdmin admin) {
		return adminService.signIn(admin);
	}

	@PutMapping("/signout/{adminId}")
	private ResponseEntity<AdminAllResponse> sinOut(@PathVariable int adminId) {
		return adminService.sinOut(adminId);
	}

	@GetMapping("/allCustomers")
	private ResponseEntity<AdminAllResponse> getAllCustomers() {
		return adminService.findAllCustomers();
	}

	@GetMapping("/unapprovedCustomers")
	private ResponseEntity<AdminAllResponse> getUnapprovedCustomers() {
		return adminService.findAllUnapprovedCustomers();
	}

	@PostMapping("/approvecustomer/{customerId}")
	private ResponseEntity<AdminAllResponse> aprroveCustomer(@PathVariable int customerId) {
		return adminService.approveCustomer(customerId);
	}

	@PutMapping("/deactivatecustomer/{customerId}")
	private ResponseEntity<AdminAllResponse> deactivateCustomer(@PathVariable int customerId) {
		return adminService.deactivateCustomer(customerId);
	}

	@GetMapping("/verifiedCustomers")
	private ResponseEntity<AdminAllResponse> getVrifiedCustomers() {
		return adminService.findVerifiedCustomers();
	}

	@GetMapping("/unVerifiedCustomers")
	private ResponseEntity<AdminAllResponse> getUnverifiedCustomers() {
		return adminService.getUnVerifiedCustomers();
	}

	@GetMapping("/approvedCustomers")
	private ResponseEntity<AdminAllResponse> getApprovedCustomers() {
		return adminService.getApprovedCustomers();
	}

	@GetMapping("/allMechanics")
	private ResponseEntity<AdminAllResponse> getAllMechanics() {
		return adminService.getAllmechanics();
	}

	@GetMapping("/unapprovedMechanics")
	private ResponseEntity<AdminAllResponse> getUnapprovedMechanics() {
		return adminService.getUnapprovedMechanics();
	}

	@GetMapping("/approvedMechanics")
	private ResponseEntity<AdminAllResponse> getApprovedMechanics() {
		return adminService.getApprovedMechanics();
	}

	@GetMapping("/verifiedMechanics")
	private ResponseEntity<AdminAllResponse> getverifiedMechanics() {
		return adminService.getVerifiedMechanics();
	}

	@GetMapping("/unVerifiedMechanics")
	private ResponseEntity<AdminAllResponse> getUnverifiedMechanics() {
		return adminService.getUnverifiedMechanics();
	}

	@PostMapping("/approveMechanic/{mechanicId}")
	private ResponseEntity<AdminAllResponse> approveMechanic(@PathVariable int mechanicId) {
		return adminService.approveMechanic(mechanicId);
	}

	@PutMapping("/deactivateMechanic/{mechanicId}")
	private ResponseEntity<AdminAllResponse> deactivateMechanic(@PathVariable int mechanicId) {
		return adminService.deactivateMechanic(mechanicId);
	}

	@GetMapping("/unverifiedTowTruckCompany")
	private ResponseEntity<AdminAllResponse> getUnverifiedTowTrucks() {
		return adminService.getUnverifiedTowTruckCompanies();
	}

	@GetMapping("/allTowTruckCompanies")
	private ResponseEntity<AdminAllResponse> getAllTowTruckCompanies() {
		return adminService.getAllTowTruckcompanies();
	}

	@GetMapping("/verifiedTowTruckCompany")
	private ResponseEntity<AdminAllResponse> getVerifiedTowTrucks() {
		return adminService.getVerifiedTowTruckCompanies();
	}

	@GetMapping("/unApprovedTowTruckCompany")
	private ResponseEntity<AdminAllResponse> getUnapprovedTowTrucks() {
		return adminService.getUnapprovedTowTruckCompanies();
	}

	@GetMapping("/approvedTowTruckCompany")
	private ResponseEntity<AdminAllResponse> getApprovedTowTrucks() {
		return adminService.getApprovedTowTruckCompanies();
	}

	@PutMapping("/approveTowTruckCompany/{towTruckCompanyId}")
	private ResponseEntity<AdminAllResponse> approveTowTruck(@PathVariable int towTruckCompanyId) {
		return adminService.approveTowTruckCompany(towTruckCompanyId);
	}

	@PutMapping("/deactivateTowTruckCompany/{towTruckCompanyId}")
	private ResponseEntity<AdminAllResponse> deactiateTowtruck(@PathVariable int towTruckCompanyId) {
		return adminService.deactivateTowTruckCompany(towTruckCompanyId);
	}

	@PostMapping("/registerMechanic")
	private ResponseEntity<AdminAllResponse> registerMechanic(@RequestBody Mechanic mechanic) {
		return adminService.registerMechanic(mechanic);
	}

	@PostMapping("/registerTowTruckCompany")
	private ResponseEntity<AdminAllResponse> registerTowTruck(@RequestBody TowTruckCompany towTruck) {
		return adminService.regiterTowTruckCompany(towTruck);
	}

	@PostMapping("/registerSupportStaff")
	private ResponseEntity<AdminAllResponse> registerSupportStaff(@RequestBody User user) {
		return adminService.registerSupportStaff(user);
	}

	@PutMapping("/deactivateStaff/{userId}")
	private ResponseEntity<AdminAllResponse> deactivateSupportStaff(@PathVariable int userId) {
		return adminService.deactivateSupportStaff(userId);
	}

	@PutMapping("/activateSupport/{userId}")
	private ResponseEntity<AdminAllResponse> activateSupportStaff(@PathVariable int userId) {
		return adminService.activateSupportStaff(userId);
	}

	@GetMapping("/getAllTickets")
	private ResponseEntity<AdminAllResponse> getCustomerSupports() {
		return adminService.getAllCustomerSupports();
	}

	@GetMapping("/getActiveCustomers")
	private ResponseEntity<AdminAllResponse> getActiveCustomers() {
		return adminService.getActiveCustomers();
	}

	@GetMapping("/getActiveMechanics")
	private ResponseEntity<AdminAllResponse> getActiveMechanics() {
		return adminService.getActiveMechanics();
	}
	@GetMapping("/getActiveTowTruckCompanies")
	private ResponseEntity<AdminAllResponse> getActiveTowTruckCompanies(){
		return adminService.getActiveTowTruckCompanies();
	}
}
