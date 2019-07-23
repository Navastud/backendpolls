package com.navastud.polls.payload;

import org.springframework.stereotype.Component;

@Component("choiceResponse")
public class ChoiceResponse {

	private Long id;

	private String text;

	private long voteCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(long voteCount) {
		this.voteCount = voteCount;
	}

}
