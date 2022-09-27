package com.garage.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.garage.dto.AdminAllResponse;
import com.garage.dto.SupportResponse;
import com.garage.entity.Customer;
import com.garage.entity.Support;
import com.garage.repository.CustomerRepository;
import com.garage.repository.SupportRepository;

@Service
public class SupportService {
	@Autowired
	SupportRepository supportRepository;
	@Autowired
	CustomerRepository customerRepository;
	AdminAllResponse adminAllResponse = new AdminAllResponse();

	public ResponseEntity<AdminAllResponse> raiseTicket(Support support) {
		Customer customer = customerRepository.findByIdAndActive(support.getCustomer().getId(), (byte) 1);
		if (customer == null) {
			adminAllResponse.setMessage("cutomer not found!");
			adminAllResponse.setCode(404);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setData(customer);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		support.setActive((byte) 1);
		support.setCreatedBy(customer.getEmail());
		support.setStatus("open");
		support.setCreationTimestamp(new Timestamp(new Date().getTime()));
		support.setCustomer(customer);
		support.setDetails(support.getDetails());
		int supnum = getSupportNumber();
		support.setSupportNumber(supnum);
		supportRepository.save(support);
		adminAllResponse.setMessage("ticket raised successfullly!");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(null);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);

	}

	public int getSupportNumber() {
		Random r = new Random(System.currentTimeMillis());
		System.out.println(1000000000 + r.nextInt(2000000000));
		return 10000 + r.nextInt(20000);
	}

	public ResponseEntity<AdminAllResponse> getCustomerSupports(int customerId) {
		Customer customer = customerRepository.findByIdAndActive(customerId, (byte) 1);
		if (customer == null) {
			adminAllResponse.setMessage("customer not found!");
			adminAllResponse.setCode(404);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			adminAllResponse.setData(customer);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		List<Support> supports = supportRepository.findByCustomerId(customerId);
		if (supports.isEmpty()) {
			adminAllResponse.setMessage("no data found!");
			adminAllResponse.setCode(200);
			adminAllResponse.setError(null);
			adminAllResponse.setData(supports);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		ArrayList<SupportResponse> supportResponses = new ArrayList<SupportResponse>();
		for (Support s : supports) {
			SupportResponse supportResponse = new SupportResponse();
			supportResponse.setActive(s.getActive());
			supportResponse.setCustomerId(customerId);
			supportResponse.setId(s.getId());
			supportResponse.setStatus(s.getStatus());
			supportResponse.setDetails(s.getDetails());
			supportResponse.setCrationtionTimestamp(s.getCreationTimestamp());
			supportResponse.setModificationTimestamp(s.getModificationTimestamp());
			supportResponse.setCreatedBY(s.getCreatedBy());
			supportResponse.setCustomerName(customer.getFirstName());
			supportResponse.setSupportNumber(s.getSupportNumber());
			supportResponses.add(supportResponse);
		}
		adminAllResponse.setMessage("customer tickets");
		adminAllResponse.setCode(200);
		adminAllResponse.setError(null);
		adminAllResponse.setData(supportResponses);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}

	public ResponseEntity<AdminAllResponse> changeTicketStatus(int ticketId) {
		Support support = supportRepository.findByIdAndActive(ticketId, (byte) 1);
		if (support == null) {
			adminAllResponse.setMessage("ticket not found!");
			adminAllResponse.setCode(404);
			adminAllResponse.setData(support);
			adminAllResponse.setError(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.NOT_FOUND);
		}
		if (support.getStatus().equals("closed")) {
			adminAllResponse.setMessage("ticket already closed!");
			adminAllResponse.setCode(200);
			adminAllResponse.setData(null);
			adminAllResponse.setError(null);
			return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
		}
		 support.setStatus("closed");
		 supportRepository.save(support);
		adminAllResponse.setMessage("ticket closed successfully!");
		adminAllResponse.setData(null);
		adminAllResponse.setError(null);
		adminAllResponse.setCode(200);
		return new ResponseEntity<AdminAllResponse>(adminAllResponse, HttpStatus.OK);
	}
}
