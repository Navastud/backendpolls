package com.navastud.polls.service;

import java.util.List;
import java.util.Map;

import com.navastud.polls.entity.Poll;
import com.navastud.polls.entity.User;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.PollRequest;
import com.navastud.polls.payload.PollResponse;
import com.navastud.polls.payload.VoteRequest;
import com.navastud.polls.security.UserPrincipal;

public interface PollService {

	PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size);

	PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page, int size);

	PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size);

	Poll createPoll(PollRequest pollRequest);

	PollResponse getPollById(Long pollId, UserPrincipal currentUser);

	PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser);

	void validatePageNumberAndSize(int page, int size);

	Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds);

	Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds);

	Map<Long, User> getPollCreatorMap(List<Poll> polls);

	long countByCreatedBy(Long id);

}
