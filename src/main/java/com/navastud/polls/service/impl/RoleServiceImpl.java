package com.navastud.polls.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.converter.RoleConverter;
import com.navastud.polls.entity.Role;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;
import com.navastud.polls.payload.RoleResponse;
import com.navastud.polls.repository.RoleRepository;
import com.navastud.polls.service.RoleService;

@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {

	@Autowired
	@Qualifier("roleRepository")
	private RoleRepository roleRepository;

	@Autowired
	@Qualifier("roleConverter")
	private RoleConverter roleConverter;

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
	public PagedResponse<RoleResponse> getAllRoles() {

		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		Page<Role> roles = roleRepository.findAll(pageable);

		return roleConverter.convertPageToPagedResponse(roles);

	}

	@Override
	public RoleResponse findById(Long id) {
		return roleConverter.convertRolToRolResponse(roleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(String.format("Role not found with id : %d ", id))));
	}

}
