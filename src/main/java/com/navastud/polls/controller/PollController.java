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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.navastud.polls.model.Poll;
import com.navastud.polls.payload.ApiResponse;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.PollRequest;
import com.navastud.polls.payload.PollResponse;
import com.navastud.polls.payload.VoteRequest;
import com.navastud.polls.security.CurrentUser;
import com.navastud.polls.security.UserPrincipal;
import com.navastud.polls.service.PollService;
import com.navastud.polls.service.UserService;
import com.navastud.polls.util.AppConstants;

@RestController
@RequestMapping("/api/polls")
public class PollController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("pollServiceImpl")
	private PollService pollService;

	private static final Logger logger = LoggerFactory.getLogger(PollController.class);

	@GetMapping
	public PagedResponse<PollResponse> getPolls(@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getAllPolls(currentUser, page, size);
	};

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {

		Poll poll = pollService.createPoll(pollRequest);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{pollId}").buildAndExpand(poll.getId())
				.toUri();
		return ResponseEntity.created(location).body(new ApiResponse(true, "Poll created Successfully"));
	}

	@PostMapping("/{pollId}/votes")
	@PreAuthorize("hasRole('USER')")
	public PollResponse castVote(@CurrentUser UserPrincipal currentUser, @PathVariable Long pollId,
			@Valid @RequestBody VoteRequest voteRequest) {

		return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
	}
}
