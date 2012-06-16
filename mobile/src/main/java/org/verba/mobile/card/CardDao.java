package org.verba.mobile.card;

import java.util.ArrayList;
import java.util.List;

import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CardDao {
	public static final String SELECT_CARD_BY_ID =
									"select * from card where _id = ?";
	public static final String SELECT_CARDS_BY_CARD_SET_ID =
									"select * from card where set_id = ? limit ?";
	public static final String INSERT_CARD = "insert into card "
														+ "(phrase, definition, set_id) "
														+ "values "
														+ "(?, ?, ?)";
	public static final String DELETE_CARD = "delete from card where _id = ?";

	private SQLiteDatabase database;

	public CardDao(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	public Card getCardById(int cardId) throws NoCardFoundException {
		Cursor cardsCursor = queryCardById(cardId);

		ensureAtLeastOneFound(cardId, cardsCursor);

		return extractCardFromCursor(cardsCursor);
	}

	public void addCard(Card card) {
		database.execSQL(INSERT_CARD,
				new Object[] {
							card.getPhrase(),
							card.getDefinition(),
							card.getCardSetId()});
	}

	public void deleteCard(Card card) {
		database.execSQL(DELETE_CARD, new Object[] { card.getId() });
	}

	public List<Card> getCardsInCardSet(int cardSetId, int maxCount) {
		Cursor cardsCursor = queryCardsByCardSetId(cardSetId, maxCount);

		return extractCardsFromCursor(cardsCursor);
	}

	private List<Card> extractCardsFromCursor(Cursor cursor) {
		List<Card> result = new ArrayList<Card>();
		while (cursor.moveToNext()) {
			result.add(extractCardFromCursor(cursor));
		}
		return result;
	}

	private Card extractCardFromCursor(Cursor cursor) {
		Card card = new Card();
		card.setId(cursor.getInt(cursor.getColumnIndex("_id")));
		card.setPhrase(cursor.getString(cursor.getColumnIndex("phrase")));
		card.setDefinition(cursor.getString(cursor.getColumnIndex("definition")));
		card.setCardSetId(cursor.getInt(cursor.getColumnIndex("set_id")));

		return card;
	}

	private void ensureAtLeastOneFound(int cardId, Cursor cardsCursor) throws NoCardFoundException {
		if (!cardsCursor.moveToNext()) {
			throw new NoCardFoundException(String.format("Card wasn't found in the database for id [%s]", cardId));
		}
	}

	private Cursor queryCardById(int cardId) {
		return database.rawQuery(SELECT_CARD_BY_ID, new String[] { String.valueOf(cardId) });
	}

	private Cursor queryCardsByCardSetId(int cardSetId, int count) {
		return database.rawQuery(SELECT_CARDS_BY_CARD_SET_ID,
				new String[] { String.valueOf(cardSetId), String.valueOf(count) });
	}

	public static class NoCardFoundException extends Exception {
		private static final long serialVersionUID = 7715190734536758220L;

		public NoCardFoundException(String message) {
			super(message);
		}
	}

	public void close() {
		database.close();
	}
}