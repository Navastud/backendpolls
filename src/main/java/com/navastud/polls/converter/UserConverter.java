package com.navastud.polls.converter;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.navastud.polls.exception.AppException;
import com.navastud.polls.model.Role;
import com.navastud.polls.model.User;
import com.navastud.polls.payload.SignUpRequest;
import com.navastud.polls.service.RoleService;

@Component("userConverter")
public class UserConverter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("roleServiceImpl")
	private RoleService roleService;

	public User convertSignUpRequestToUser(SignUpRequest signUpRequest) {

		User user = new User();
		user.setName(signUpRequest.getName());
		user.setUsername(signUpRequest.getUsername());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

		Set<Role> roles = signUpRequest.getRoles().stream().map(role -> roleService.findByName(role.getName())
				.orElseThrow(() -> new AppException("User Role not set 2"))).collect(Collectors.toSet());

		user.setRoles(roles);

		return user;
	}

}
