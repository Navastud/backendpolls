package com.navastud.polls.converter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.navastud.polls.entity.Choice;
import com.navastud.polls.entity.Poll;
import com.navastud.polls.entity.User;
import com.navastud.polls.payload.ChoiceResponse;
import com.navastud.polls.payload.PollRequest;
import com.navastud.polls.payload.PollResponse;
import com.navastud.polls.payload.UserSummary;

@Component("pollConverter")
public class PollConverter {

	@Autowired
	@Qualifier("pollResponse")
	private PollResponse pollResponse;

	@Autowired
	@Qualifier("choiceConverter")
	private ChoiceConverter choiceConverter;

	public PollResponse convertPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, User creator,
			Long userVote) {

		pollResponse.setId(poll.getId());
		pollResponse.setQuestion(poll.getQuestion());
		pollResponse.setCreationDateTime(poll.getCreatedAt());
		pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
		pollResponse.setIsExpired(poll.getExpirationDateTime().isBefore(LocalDateTime.now()));

		List<ChoiceResponse> choiceResponses = poll.getChoices().stream()
				.map(choice -> choiceConverter.convertChoiceToChoiceResponse(choice, choiceVotesMap))
				.collect(Collectors.toList());

		pollResponse.setChoices(choiceResponses);
		UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
		pollResponse.setCreatedBy(creatorSummary);

		if (userVote != null) {
			pollResponse.setSelectedChoice(userVote);
		}

		long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
		pollResponse.setTotalVotes(totalVotes);

		return pollResponse;
	}

	public Poll convertPollRequestToPoll(PollRequest pollRequest) {

		Poll poll = new Poll();
		poll.setQuestion(pollRequest.getQuestion());
		pollRequest.getChoices().forEach(choiceRequest -> {
			poll.addChoice(new Choice(choiceRequest.getText()));
		});

		poll.setExpirationDateTime(LocalDateTime.now().plusDays(pollRequest.getPollLength().getDays())
				.plusHours(pollRequest.getPollLength().getHours()));

		return poll;

	}

}
