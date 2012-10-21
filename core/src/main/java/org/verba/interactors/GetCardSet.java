package org.verba.interactors;

import java.util.List;

import org.verba.CardSet;
import org.verba.cardset.CardSetRepository;

public class GetCardSet {
	private CardSetRepository cardSetRepository;

	public GetCardSet(CardSetRepository cardSetRepository) {
		this.cardSetRepository = cardSetRepository;
	}

	public List<CardSet> all() {
		return cardSetRepository.getAllCardSets();
	}

}
