package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.AdminAllResponse;
import com.garage.entity.Support;
import com.garage.service.SupportService;

@RestController
@RequestMapping("support")
public class SupportController {
	@Autowired
	SupportService supportService;

	@PostMapping("/raiseTicket")
	private ResponseEntity<AdminAllResponse> raiseTicket(@RequestBody Support support) {
		return supportService.raiseTicket(support);
	}

	@GetMapping("/getCustomerSupports/{customerId}")
	private ResponseEntity<AdminAllResponse> getCustomerSupports(@PathVariable int customerId) {
		return supportService.getCustomerSupports(customerId);
	}
	@PostMapping("/changeTicketStatus/{ticketId}")
	private ResponseEntity<AdminAllResponse> changeTicketstatus(@PathVariable int ticketId){
		 return supportService.changeTicketStatus(ticketId);
	}
}
