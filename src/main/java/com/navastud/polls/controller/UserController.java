package com.navastud.polls.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navastud.polls.entity.User;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.PollResponse;
import com.navastud.polls.payload.UserIdentityAvailability;
import com.navastud.polls.payload.UserProfile;
import com.navastud.polls.payload.UserSummary;
import com.navastud.polls.security.CurrentUser;
import com.navastud.polls.security.UserPrincipal;
import com.navastud.polls.service.PollService;
import com.navastud.polls.service.UserService;
import com.navastud.polls.service.VoteService;
import com.navastud.polls.util.AppConstants;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("voteServiceImpl")
	private VoteService voteService;

	@Autowired
	@Qualifier("pollServiceImpl")
	private PollService pollService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

	@GetMapping("/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {

		Boolean isAvailable = !userService.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/checkEmilAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {

		Boolean isAvailable = !userService.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userService.findByUsername(username);
		long pollCount = pollService.countByCreatedBy(user.getId());
		long voteCount = voteService.countByUserId(user.getId());

		UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(),
				pollCount, voteCount);

		return userProfile;
	}

	@GetMapping("/{username}/polls")
	public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

		return pollService.getPollsCreatedBy(username, currentUser, page, size);
	}

	@GetMapping("/{username}/votes")
	public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

		return pollService.getPollsVotedBy(username, currentUser, page, size);
	}
}
