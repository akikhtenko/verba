package org.verba.card;

import java.util.List;

import org.verba.Card;

public interface CardRepository {
	void addCard(Card card);
	List<Card> getCardsFromCardSet(int cardSetId, int maxCount);
	Card getCardById(int cardId);
}
