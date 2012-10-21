package org.verba.interactors;

import org.verba.CardSet;
import org.verba.cardset.CardSetRepository;

public class AddCardSet {
	private CardSetRepository cardSetRepository;

	public AddCardSet(CardSetRepository cardSetRepository) {
		this.cardSetRepository = cardSetRepository;
	}

	public void with(String cardSetName) {
		//TODO: add check for existing card set with the same name
		CardSet cardSet = new CardSet();
		cardSet.setName(cardSetName);

		cardSetRepository.addCardSet(cardSet);
	}
}
