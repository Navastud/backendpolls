package com.navastud.polls.service;

import java.util.List;

import com.navastud.polls.entity.User;
import com.navastud.polls.payload.SignUpRequest;

public interface UserService {

	User findByUsernameOrEmail(String username, String email);

	User findByEmail(String email);

	User findByUsername(String username);

	User findById(Long id);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	List<User> findByIdIn(List<Long> userIds);

	User createUser(SignUpRequest signUpRequest);

	User getOne(Long id);

}
