package org.verba.interactors;

import java.util.List;

import org.verba.Card;
import org.verba.boundary.CardRetrieval;
import org.verba.card.CardRepository;

public class SimpleCardRetrieval implements CardRetrieval {
	private CardRepository cardRepository;

	public SimpleCardRetrieval(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	@Override
	public Card getCardById(int cardId) {
		return cardRepository.getCardById(cardId);
	}

	@Override
	public List<Card> getCardsFromCardSet(int cardSetId, int limit) {
		return cardRepository.getCardsFromCardSet(cardSetId, limit);
	}

}
