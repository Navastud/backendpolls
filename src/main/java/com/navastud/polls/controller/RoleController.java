package com.navastud.polls.controller;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.navastud.polls.entity.Role;
import com.navastud.polls.payload.ApiResponse;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;
import com.navastud.polls.payload.RoleResponse;
import com.navastud.polls.service.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	@Qualifier("roleServiceImpl")
	private RoleService roleService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@GetMapping
	public PagedResponse<RoleResponse> getRoles() {

		LOGGER.info("METHOD: getRoles() -- MAPPING: /roles");

		return roleService.getAllRoles();
	}

	@GetMapping("/{id}")
	public RoleResponse getRole(@PathVariable(value = "id") Long id) {

		LOGGER.info("METHOD: getRole() -- MAPPING: /roles/{id} -- PARAMS: id=" + id);

		return roleService.findById(id);
	}

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequest roleRequest) {

		LOGGER.info("METHOD: createRole() -- MAPPING: /roles -- PARAMS: roleRequest=" + roleRequest);

		Role role = roleService.createRole(roleRequest);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{roleId}").buildAndExpand(role.getId())
				.toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "Role created Successfully"));
	}

}
