package org.verba.cardset;

import java.util.List;

import org.verba.CardSet;

public interface CardSetRepository {
	List<CardSet> getAllCardSets();

	int addCardSet(CardSet cardSet);
}
