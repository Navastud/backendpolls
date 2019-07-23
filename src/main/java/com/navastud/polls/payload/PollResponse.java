package com.navastud.polls.payload;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

@Component("pollResponse")
public class PollResponse {

	private Long id;

	private String question;

	private List<ChoiceResponse> choices;

	private UserSummary createdBy;

	private LocalDateTime creationDateTime;

	private LocalDateTime expirationDateTime;

	private Boolean isExpired;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Long selectedChoice;

	private Long totalVotes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<ChoiceResponse> getChoices() {
		return choices;
	}

	public void setChoices(List<ChoiceResponse> choices) {
		this.choices = choices;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public LocalDateTime getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(LocalDateTime expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public Boolean getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(Boolean isExpired) {
		this.isExpired = isExpired;
	}

	public Long getSelectedChoice() {
		return selectedChoice;
	}

	public void setSelectedChoice(Long selectedChoice) {
		this.selectedChoice = selectedChoice;
	}

	public Long getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(Long totalVotes) {
		this.totalVotes = totalVotes;
	}
}
