package com.navastud.polls.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoleRequest {

	@NotBlank
	@Size(max = 60)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
