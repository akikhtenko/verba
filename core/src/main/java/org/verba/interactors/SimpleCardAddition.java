package org.verba.interactors;

import org.verba.Card;
import org.verba.boundary.CardAddition;
import org.verba.card.CardRepository;

public class SimpleCardAddition implements CardAddition {
	private CardRepository cardRepository;

	public SimpleCardAddition(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public void addCard(int cardSetId, String phrase, String definition) {
		Card card = new Card();
		card.setCardSetId(cardSetId);
		card.setPhrase(phrase);
		card.setDefinition(definition);

		cardRepository.addCard(card);
	}

}
