package com.navastud.polls.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.navastud.polls.converter.UserConverter;
import com.navastud.polls.exception.ResourceNotFoundException;
import com.navastud.polls.model.User;
import com.navastud.polls.payload.SignUpRequest;
import com.navastud.polls.repository.UserRepository;
import com.navastud.polls.service.UserService;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

	@Autowired
	@Qualifier("userConverter")
	private UserConverter userConverter;

	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public User createUser(SignUpRequest signUpRequest) {
		return userRepository.save(userConverter.convertSignUpRequestToUser(signUpRequest));
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
	}

	@Override
	public User findByUsernameOrEmail(String username, String email) {
		return userRepository.findByUsernameOrEmail(username, email).orElseThrow(() -> new UsernameNotFoundException(
				String.format("User not found with username or email : %s", (username.isEmpty() ? email : username))));
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
	}

	@Override
	public List<User> findByIdIn(List<Long> userIds) {
		return userRepository.findByIdIn(userIds);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with id : %d ", id)));
	}

	@Override
	public User getOne(Long id) {
		return userRepository.getOne(id);
	}
}