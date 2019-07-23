package com.navastud.polls.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.navastud.polls.entity.Role;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.RoleResponse;

@Component("roleConverter")
public class RoleConverter extends BaseConverter<RoleResponse> {

	@Autowired
	@Qualifier("roleResponse")
	private RoleResponse roleResponse;

	public RoleResponse convertRolToRolResponse(Role role) {

		roleResponse.setId(role.getId());
		roleResponse.setName(role.getName().name());

		return roleResponse;
	}

	@Override
	public PagedResponse<RoleResponse> convertPageToPagedResponse(Page<?> pages) {

		List<RoleResponse> content = pages.stream().map(role -> convertRolToRolResponse((Role) role))
				.collect(Collectors.toList());

		return pagedConverter.convertPageToPagedResponse(pages, content);
	}

}
