package com.garage.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.garage.Exceptions.ResourceNotFoundException;
import com.garage.entity.Customer;
import com.garage.entity.Mechanic;
import com.garage.entity.SuperAdmin;
import com.garage.entity.TowTruckCompany;
import com.garage.entity.User;
import com.garage.repository.CustomerRepository;
import com.garage.repository.MechanicRepository;
import com.garage.repository.SuperAdminRepository;
import com.garage.repository.TowTruckCompanyRepository;
import com.garage.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private MechanicRepository mechanicRepository;
	@Autowired
	private TowTruckCompanyRepository towTruckRepository;
    @Autowired
    private SuperAdminRepository adminRepository;
    @Autowired
    private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Customer getCustomer = customerRepository.findByEmail(username);
		Mechanic getMechanic = mechanicRepository.findByEmail(username);
		TowTruckCompany getTowTruck = towTruckRepository.findByEmail(username);
		SuperAdmin admin=adminRepository.findByEmail(username);
		User user1=userRepository.findByEmail(username);
		// loading user from database by userName
		Customer user = null;
		Mechanic mechanic = null;
		TowTruckCompany towTruck = null;
		SuperAdmin superAdmin=null;
		User user2=null;
		if(getCustomer!=null) {
			user = this.customerRepository.findByEmailAndActive(username, (byte) 1)
					.orElseThrow(() -> new ResourceNotFoundException("user", "email", "username"));
			
			return user;
		} else if(getMechanic!=null) {
			mechanic = this.mechanicRepository.findByEmailAndActive(username, (byte) 1)
					.orElseThrow(() -> new ResourceNotFoundException("user", "email", "username"));
			
			return mechanic;
		} else if(getTowTruck!=null) {
			towTruck = this.towTruckRepository.findByEmailAndActive(username, (byte) 1)
					.orElseThrow(() -> new ResourceNotFoundException("user", "email", "username"));
			return towTruck;
		}
			else if(admin!=null) {
				superAdmin = this.adminRepository.findByEmailAndActive(username, (byte) 1)
						.orElseThrow(() -> new ResourceNotFoundException("user", "email", "username"));
				return superAdmin;
			}
				else if(user1!=null) 
					user2 = this.userRepository.findByEmailAndActive(username, (byte) 1)
							.orElseThrow(() -> new ResourceNotFoundException("user", "email", "username"));
					return superAdmin;
	}

}
