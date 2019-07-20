package com.navastud.polls.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.navastud.polls.entity.Choice;
import com.navastud.polls.entity.ChoiceVoteCount;
import com.navastud.polls.entity.Poll;
import com.navastud.polls.entity.User;
import com.navastud.polls.entity.Vote;
import com.navastud.polls.exception.BadRequestException;
import com.navastud.polls.exception.ResourceNotFoundException;
import com.navastud.polls.payload.PagedResponse;
import com.navastud.polls.payload.PollRequest;
import com.navastud.polls.payload.PollResponse;
import com.navastud.polls.payload.VoteRequest;
import com.navastud.polls.repository.PollRepository;
import com.navastud.polls.security.UserPrincipal;
import com.navastud.polls.service.PollService;
import com.navastud.polls.service.UserService;
import com.navastud.polls.service.VoteService;
import com.navastud.polls.util.AppConstants;
import com.navastud.polls.util.ModelMapper;

@Service("pollServiceImpl")
public class PollServiceImpl implements PollService {

	@Autowired
	@Qualifier("pollRepository")
	private PollRepository pollRepository;

	@Autowired
	@Qualifier("voteServiceImpl")
	private VoteService voteService;

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(PollServiceImpl.class);

	@Override
	public PagedResponse<PollResponse> getAllPolls(UserPrincipal currentUser, int page, int size) {

		validatePageNumberAndSize(page, size);

		// Retrieve Polls
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Poll> polls = pollRepository.findAll(pageable);

		if (polls.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(),
					polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
		}

		// Map Polls to PollResponses containing vote counts and poll creator details
		List<Long> pollIds = polls.map(Poll::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());

		List<PollResponse> pollResponses = polls.map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, choiceVoteCountMap, creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).getContent();

		return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(),
				polls.getTotalPages(), polls.isLast());

	}

	@Override
	public PagedResponse<PollResponse> getPollsCreatedBy(String username, UserPrincipal currentUser, int page,
			int size) {

		validatePageNumberAndSize(page, size);

		User user = userService.findByUsername(username);

		// Retrieve all polls created by the given username
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Poll> polls = pollRepository.findByCreatedBy(user.getId(), pageable);

		if (polls.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), polls.getNumber(), polls.getSize(),
					polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
		}

		// Map Polls to PollResponses containing vote counts and poll creator details
		List<Long> pollIds = polls.map(Poll::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);

		List<PollResponse> pollResponses = polls.map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, choiceVoteCountMap, user,
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).getContent();

		return new PagedResponse<>(pollResponses, polls.getNumber(), polls.getSize(), polls.getTotalElements(),
				polls.getTotalPages(), polls.isLast());

	}

	@Override
	public PagedResponse<PollResponse> getPollsVotedBy(String username, UserPrincipal currentUser, int page, int size) {

		validatePageNumberAndSize(page, size);

		User user = userService.findByUsername(username);

		// Retrieve all pollIds in which the given username has voted
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<Long> userVotedPollIds = voteService.findVotedPollIdsByUserId(user.getId(), pageable);

		if (userVotedPollIds.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), userVotedPollIds.getNumber(),
					userVotedPollIds.getSize(), userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(),
					userVotedPollIds.isLast());
		}

		// Retrieve all poll details from the voted pollIds.
		List<Long> pollIds = userVotedPollIds.getContent();

		Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
		List<Poll> polls = pollRepository.findByIdIn(pollIds, sort);

		// Map Polls to PollResponses containing vote counts and poll creator details
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(polls);

		List<PollResponse> pollResponses = polls.stream().map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll, choiceVoteCountMap, creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).collect(Collectors.toList());

		return new PagedResponse<>(pollResponses, userVotedPollIds.getNumber(), userVotedPollIds.getSize(),
				userVotedPollIds.getTotalElements(), userVotedPollIds.getTotalPages(), userVotedPollIds.isLast());

	}

	@Override
	public Poll createPoll(PollRequest pollRequest) {

		Poll poll = new Poll();
		poll.setQuestion(pollRequest.getQuestion());

		pollRequest.getChoices().forEach(choiceRequest -> {
			poll.addChoice(new Choice(choiceRequest.getText()));
		});

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime expirationDateTime = now.plusDays(pollRequest.getPollLength().getDays())
				.plusHours(pollRequest.getPollLength().getHours());

		poll.setExpirationDateTime(expirationDateTime);

		return pollRepository.save(poll);
	}

	@Override
	public PollResponse getPollById(Long pollId, UserPrincipal currentUser) {

		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		// Retrieve Vote Counts of every choice belonging to the current poll
		List<ChoiceVoteCount> votes = voteService.countByPollIdGroupByChoiceId(pollId);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve poll creator details
		User creator = userService.findById(poll.getCreatedBy());

		// Retrieve vote done by logged in user
		Vote userVote = null;
		if (currentUser != null) {
			userVote = voteService.findByUserIdAndPollId(currentUser.getId(), pollId);
		}

		return ModelMapper.mapPollToPollResponse(poll, choiceVotesMap, creator,
				userVote != null ? userVote.getChoice().getId() : null);
	}

	@Override
	public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UserPrincipal currentUser) {
		Poll poll = pollRepository.findById(pollId)
				.orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

		if (poll.getExpirationDateTime().isBefore(LocalDateTime.now())) {
			throw new BadRequestException("Sorry! This Poll has already expired");
		}

		User user = userService.getOne(currentUser.getId());

		Choice selectedChoice = poll.getChoices().stream()
				.filter(choice -> choice.getId().equals(voteRequest.getChoiceId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

		Vote vote = new Vote();
		vote.setPoll(poll);
		vote.setUser(user);
		vote.setChoice(selectedChoice);

		try {
			vote = voteService.createVote(vote);
		} catch (DataIntegrityViolationException ex) {
			logger.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
			throw new BadRequestException("Sorry! You have already cast your vote in this poll");
		}

		// -- Vote Saved, Return the updated Poll Response now --

		// Retrieve Vote Counts of every choice belonging to the current poll
		List<ChoiceVoteCount> votes = voteService.countByPollIdGroupByChoiceId(pollId);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		// Retrieve poll creator details
		User creator = userService.findById(poll.getCreatedBy());

		return ModelMapper.mapPollToPollResponse(poll, choiceVotesMap, creator, vote.getChoice().getId());

	}

	@Override
	public void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size > AppConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
		}
	}

	@Override
	public Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
		// Retrieve Vote Counts of every Choice belonging to the given pollIds
		List<ChoiceVoteCount> votes = voteService.countByPollIdInGroupByChoiceId(pollIds);

		Map<Long, Long> choiceVotesMap = votes.stream()
				.collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

		return choiceVotesMap;
	}

	@Override
	public Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> pollIds) {

		// Retrieve Votes done by the logged in user to the given pollIds
		Map<Long, Long> pollUserVoteMap = null;
		if (currentUser != null) {
			List<Vote> userVotes = voteService.findByUserIdAndPollIdIn(currentUser.getId(), pollIds);

			pollUserVoteMap = userVotes.stream()
					.collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
		}
		return pollUserVoteMap;
	}

	@Override
	public Map<Long, User> getPollCreatorMap(List<Poll> polls) {

		// Get Poll Creator details of the given list of polls
		List<Long> creatorIds = polls.stream().map(Poll::getCreatedBy).distinct().collect(Collectors.toList());

		List<User> creators = userService.findByIdIn(creatorIds);
		Map<Long, User> creatorMap = creators.stream().collect(Collectors.toMap(User::getId, Function.identity()));

		return creatorMap;
	}

	@Override
	public long countByCreatedBy(Long id) {
		return pollRepository.countByCreatedBy(id);
	}
}
