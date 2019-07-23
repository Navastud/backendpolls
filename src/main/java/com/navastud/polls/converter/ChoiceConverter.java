package com.navastud.polls.converter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.navastud.polls.entity.Choice;
import com.navastud.polls.payload.ChoiceResponse;

@Component("choiceConverter")
public class ChoiceConverter {

	@Autowired
	@Qualifier("choiceResponse")
	private ChoiceResponse choiceResponse;

	public ChoiceResponse convertChoiceToChoiceResponse(Choice choice, Map<Long, Long> choiceVotesMap) {

		choiceResponse.setId(choice.getId());
		choiceResponse.setText(choice.getText());

		if (choiceVotesMap.containsKey(choice.getId())) {
			choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
		} else {
			choiceResponse.setVoteCount(0);
		}

		return choiceResponse;
	}

}
