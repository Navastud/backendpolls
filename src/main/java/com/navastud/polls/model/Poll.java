package com.navastud.polls.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.navastud.polls.model.audit.UserDateAudit;

@Entity
@Table(name = "polls")
public class Poll extends UserDateAudit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 140)
	private String question;

	@OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Size(min = 2, max = 6)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 30)
	private List<Choice> choices = new ArrayList<Choice>();

	@NotNull
	private LocalDateTime expirationDateTime;

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

	public List<Choice> getChoices() {
		return choices;
	}

	public void addChoice(Choice choice) {
		this.choices.add(choice);
		choice.setPoll(this);
	}

	public void removeChoice(Choice choice) {
		this.choices.remove(choice);
		choice.setPoll(null);
	}

	public LocalDateTime getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(LocalDateTime expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

}
