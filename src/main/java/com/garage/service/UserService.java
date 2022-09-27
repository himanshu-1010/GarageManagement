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
import org.springframework.stereotype.Service;

import com.garage.authentication.JwtTokenHelper;
import com.garage.dto.Response;
import com.garage.entity.User;
import com.garage.repository.RoleRepository;
import com.garage.repository.UserRepository;

@Service
public class UserService {
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
	Response response = new Response();

	public ResponseEntity<Response> signIn(User user) {
		User user1 = userRepository.findByEmail(user.getEmail().trim());
		if (user1 == null) {
			response.setMessage("user not found with this email!");
			response.setCode(404);
			response.setResult(user1);
			response.setToken(null);
			response.setError(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}
		 if (user1.getIsActive() == 0) {
			response.setMessage("user account deactivated!");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setResult(user1);
			response.setToken(null);
			return new ResponseEntity<Response>(response,HttpStatus.UNAUTHORIZED);
		}
		try {
			this.authenticate(user.getEmail().trim(), user.getPassword());
		} catch (Exception e) {
			System.out.println(e);
			response.setMessage("Please try with correct password");
			response.setCode(401);
			response.setError(HttpStatus.UNAUTHORIZED.name());
			response.setToken(null);
			response.setResult(null);
			return new ResponseEntity<Response>(response, HttpStatus.UNAUTHORIZED);
		}

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail().trim());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		System.out.println(token);
		user1.setIsLoggedIn((byte) 1);
		user1.setLastLoggedInTimestamp(new Timestamp(new Date().getTime()));
		userRepository.save(user1);
		response.setMessage("sign-in successfully");
		response.setCode(200);
		response.setError(null);
		response.setToken(token);
		response.setResult(user1);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	private void authenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		this.authenticationManager.authenticate(authenticationToken);

	}

	public ResponseEntity<Response> signOut(int userId) {
		User user = userRepository.findByIdAndActive(userId, (byte) 1);
		if (user == null) {
			response.setMessage("user not found!");
			response.setCode(404);
			response.setError(HttpStatus.NOT_FOUND.name());
			response.setResult(user);
			response.setToken(null);
			return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
		}
		user.setIsLoggedIn((byte) 0);
		user.setLastLoggedOutTimestamp(new Timestamp(new Date().getTime()));
		userRepository.save(user);
		response.setMessage("loggedout successfully!");
		response.setCode(200);
		response.setResult(null);
		response.setError(null);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
}
