package org.verba.interactors;

import java.util.List;

import org.verba.Card;
import org.verba.card.CardRepository;

public class GetCards {
	private CardRepository cardRepository;

	public GetCards(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public List<Card> fromCardSet(int cardSetId, int limit) {
		return cardRepository.getCardsFromCardSet(cardSetId, limit);
	}

}
