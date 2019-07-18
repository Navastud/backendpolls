package com.navastud.polls.converter;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.exception.AppException;
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
		user.setRoles(Collections.singleton(
				roleService.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set 2"))));

		return user;
	}

}
