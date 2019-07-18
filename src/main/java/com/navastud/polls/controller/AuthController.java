package com.navastud.polls.controller;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.exception.AppException;
import com.navastud.polls.model.Role;
import com.navastud.polls.model.User;
import com.navastud.polls.payload.ApiResponse;
import com.navastud.polls.payload.JwtAuthenticationResponse;
import com.navastud.polls.payload.LoginRequest;
import com.navastud.polls.payload.SignUpRequest;
import com.navastud.polls.security.JwtTokenProvider;
import com.navastud.polls.service.RoleService;
import com.navastud.polls.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("roleServiceImpl")
	private RoleService roleService;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);

		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

		if (userService.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
		}

		if (userService.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Email address already in use!"), HttpStatus.BAD_REQUEST);
		}

		User result = userService.createUser(signUpRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
				.buildAndExpand(result.getName()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
	}
}
