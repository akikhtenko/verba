package org.verba.interactors;

import java.util.List;

import org.verba.Card;
import org.verba.card.CardRepository;

public class GetCard {
	private CardRepository cardRepository;

	public GetCard(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public Card withId(int cardId) {
		return cardRepository.getCardById(cardId);
	}

	public List<Card> getCardsFromCardSet(int cardSetId, int limit) {
		return cardRepository.getCardsFromCardSet(cardSetId, limit);
	}

}
