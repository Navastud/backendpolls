package com.navastud.polls.payload;

public class UserIdentityAvailability {

	private Boolean available;

	public UserIdentityAvailability(Boolean available) {
		super();
		this.available = available;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

}
