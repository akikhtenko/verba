package org.verba.interactors;

import java.util.List;

import org.verba.CardSet;
import org.verba.boundary.CardSetRetrieval;
import org.verba.cardset.CardSetRepository;

public class SimpleCardSetRetrieval implements CardSetRetrieval {
	private CardSetRepository cardSetRepository;

	public SimpleCardSetRetrieval(CardSetRepository cardSetRepository) {
		this.cardSetRepository = cardSetRepository;
	}

	@Override
	public List<CardSet> getAllCardSets() {
		return cardSetRepository.getAllCardSets();
	}

}
