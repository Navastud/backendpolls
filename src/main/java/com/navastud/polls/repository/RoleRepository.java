package com.navastud.polls.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.entity.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Serializable> {

	Optional<Role> findByName(RoleName roleName);
}
