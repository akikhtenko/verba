package org.verba.interactors;

import org.verba.Card;
import org.verba.card.CardRepository;

public class AddCard {
	private CardRepository cardRepository;

	public AddCard(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public void with(int cardSetId, String phrase, String definition) {
		Card card = new Card();
		card.setCardSetId(cardSetId);
		card.setPhrase(phrase);
		card.setDefinition(definition);

		cardRepository.addCard(card);
	}

}
