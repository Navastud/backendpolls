package com.navastud.polls.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.navastud.polls.entity.ChoiceVoteCount;
import com.navastud.polls.entity.Vote;
import com.navastud.polls.repository.VoteRepository;
import com.navastud.polls.service.VoteService;

@Service("voteServiceImpl")
public class VoteServiceImpl implements VoteService {

	@Autowired
	@Qualifier("voteRepository")
	private VoteRepository voteRepository;

	@Override
	public long countByUserId(Long userId) {
		return voteRepository.countByUserId(userId);
	}

	@Override
	public Page<Long> findVotedPollIdsByUserId(Long userId, Pageable pageable) {
		return voteRepository.findVotedPollIdsByUserId(userId, pageable);
	}

	@Override
	public Vote findByUserIdAndPollId(Long userId, Long pollId) {
		return voteRepository.findByUserIdAndPollId(userId, pollId);
	}

	@Override
	public Vote createVote(Vote vote) {
		return voteRepository.save(vote);
	}

	@Override
	public List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(List<Long> pollIds) {
		return voteRepository.countByPollIdInGroupByChoiceId(pollIds);
	}

	@Override
	public List<ChoiceVoteCount> countByPollIdGroupByChoiceId(Long pollId) {
		return voteRepository.countByPollIdGroupByChoiceId(pollId);
	}

	@Override
	public List<Vote> findByUserIdAndPollIdIn(Long userId, List<Long> pollIds) {
		return voteRepository.findByUserIdAndPollIdIn(userId, pollIds);
	}

}
