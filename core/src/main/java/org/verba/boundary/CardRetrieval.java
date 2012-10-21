package org.verba.boundary;

import java.util.List;

import org.verba.Card;

public interface CardRetrieval {
	Card getCardById(int cardId);
	List<Card> getCardsFromCardSet(int cardSetId, int limit);
}
