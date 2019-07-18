package com.navastud.polls.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.navastud.polls.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Serializable> {

	Optional<User> findByUsernameOrEmail(String username, String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	List<User> findByIdIn(List<Long> userIds);

}