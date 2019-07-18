package com.navastud.polls.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.navastud.polls.service.UserService;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	// Let people login with either username or email
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		return UserPrincipal.create(userService.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail));

	}

	// This method is used by JWTAuthenticationFilter
	@Transactional
	public UserDetails loadUserById(Long id) {
		return UserPrincipal.create(userService.findById(id));
	}
}
