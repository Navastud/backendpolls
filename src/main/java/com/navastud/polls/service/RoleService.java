package com.navastud.polls.service;

import java.util.Optional;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.entity.Role;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;
import com.navastud.polls.payload.RoleResponse;

public interface RoleService {

	Optional<Role> findByName(RoleName roleName);

	Role createRole(RoleRequest roleRequest);

	PagedResponse<RoleResponse> getAllRoles();

	RoleResponse findById(Long id);
}
