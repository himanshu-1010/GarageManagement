package com.garage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garage.dto.Response;
import com.garage.entity.User;
import com.garage.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping("/signIn")
	private ResponseEntity<Response> sinIn(@RequestBody User user) {
		return userService.signIn(user);
	}

	@PutMapping("/signout/{userId}")
	private ResponseEntity<Response> signOut(@PathVariable int userId) {
		return userService.signOut(userId);
	}
}
