package com.navastud.polls.service;

import java.util.Optional;

import com.navastud.polls.constant.RoleName;
import com.navastud.polls.model.Role;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;

public interface RoleService {

	Optional<Role> findByName(RoleName roleName);

	Role createRole(RoleRequest roleRequest);

	PagedResponse<Role> getAllRoles();
}
