package com.navastud.polls.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.model.Role;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;
import com.navastud.polls.repository.RoleRepository;
import com.navastud.polls.service.RoleService;

@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {

	@Autowired
	@Qualifier("roleRepository")
	RoleRepository roleRepository;

	@Override
	public Optional<Role> findByName(RoleName roleName) {
		return roleRepository.findByName(roleName);
	}

	@Override
	public Role createRole(RoleRequest roleRequest) {

		RoleName roleName = RoleName.valueOf(roleRequest.getName());
		Role role = new Role();
		role.setName(roleName);

		return roleRepository.save(role);
	}

	@Override
	public PagedResponse<Role> getAllRoles() {

		// Retrieve Polls
		Pageable pageable = PageRequest.of(1, 10, Sort.Direction.DESC, "createdAt");
		Page<Role> roles = roleRepository.findAll(pageable);

		if (roles.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), roles.getNumber(), roles.getSize(),
					roles.getTotalElements(), roles.getTotalPages(), roles.isLast());
		}

		return new PagedResponse<>(roles.stream().map(r -> r).collect(Collectors.toList()), 1, roles.getSize(), 1, 1,
				false);

	}

}
