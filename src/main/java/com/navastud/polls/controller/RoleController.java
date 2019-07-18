package com.navastud.polls.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.navastud.polls.model.Role;
import com.navastud.polls.payload.ApiResponse;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleRequest;
import com.navastud.polls.service.RoleService;

@Controller
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	@Qualifier("roleServiceImpl")
	private RoleService roleService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequest roleRequest) {

		Role role = roleService.createRole(roleRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{roleId}").buildAndExpand(role.getId())
				.toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "Role created Successfully"));
	}

	@GetMapping
	public PagedResponse<Role> getRoles() {
		return roleService.getAllRoles();
	}

}
