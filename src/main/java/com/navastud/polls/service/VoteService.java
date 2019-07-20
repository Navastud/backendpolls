package com.navastud.polls.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.navastud.polls.entity.ChoiceVoteCount;
import com.navastud.polls.entity.Vote;

public interface VoteService {

	long countByUserId(Long userId);

	Page<Long> findVotedPollIdsByUserId(Long userId, Pageable pageable);

	Vote findByUserIdAndPollId(Long userId, Long pollId);

	Vote createVote(Vote vote);

	List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(List<Long> pollIds);

	List<ChoiceVoteCount> countByPollIdGroupByChoiceId(Long pollId);

	List<Vote> findByUserIdAndPollIdIn(Long userId, List<Long> pollIds);

}
