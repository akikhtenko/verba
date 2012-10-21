package org.verba.interactors;

import org.verba.CardSet;
import org.verba.boundary.CardSetAddition;
import org.verba.cardset.CardSetRepository;

public class SimpleCardSetAddition implements CardSetAddition {
	private CardSetRepository cardSetRepository;

	public SimpleCardSetAddition(CardSetRepository cardSetRepository) {
		this.cardSetRepository = cardSetRepository;
	}

	@Override
	public void addCardSet(String cardSetName) {
		//TODO: add check for existing card set with the same name
		CardSet cardSet = new CardSet();
		cardSet.setName(cardSetName);

		cardSetRepository.addCardSet(cardSet);
	}
}
