package org.verba.mobile.card;

import java.util.ArrayList;
import java.util.List;

import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CardSetDao {
	public static final String SELECT_ALL_CARD_SETS = "select * from card_set order by name";
	public static final String SELECT_CARD_SET_BY_ID = "select * from card_set where _id = ?";
	public static final String SELECT_CARD_SET_BY_NAME = "select * from card_set where name = ?";
	public static final String INSERT_CARD_SET = "insert into card_set (name) values (?)";
	public static final String DELETE_CARD_SET = "delete from card_set where _id = ?";

	private SQLiteDatabase database;

	public CardSetDao(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	public List<CardSet> getAllCardSets() {
		Cursor cardSetsCursor = queryAllCardSets();

		return extractCardSetsFromCursor(cardSetsCursor);
	}

	private List<CardSet> extractCardSetsFromCursor(Cursor cursor) {
		List<CardSet> result = new ArrayList<CardSet>();
		while (cursor.moveToNext()) {
			result.add(extractCardSetFromCursor(cursor));
		}
		return result;
	}

	public CardSet getCardSetById(int cardSetId) throws NoCardSetFoundException {
		Cursor cardSetsCursor = queryCardSetById(cardSetId);

		ensureAtLeastOneFound(cardSetId, cardSetsCursor);

		return extractCardSetFromCursor(cardSetsCursor);
	}

	private Cursor queryCardSetById(int cardSetId) {
		return database.rawQuery(SELECT_CARD_SET_BY_ID, new String[] { String.valueOf(cardSetId) });
	}

	public int addCardSet(CardSet cardSet) {
		database.execSQL(INSERT_CARD_SET,
				new String[] { cardSet.getName() });

		return getInsertedCardSetId(cardSet);
	}

	private int getInsertedCardSetId(CardSet cardSet) {
		Cursor dictionaryCursor = queryCardSetByName(cardSet.getName());

		if (dictionaryCursor.moveToNext()) {
			return dictionaryCursor.getInt(dictionaryCursor.getColumnIndex("_id"));
		} else {
			return -1;
		}
	}

	public void deleteCardSet(CardSet cardSet) {
		database.execSQL(DELETE_CARD_SET, new Object[] { cardSet.getId() });
	}

	private CardSet extractCardSetFromCursor(Cursor cardSetsCursor) {
		CardSet cardSet = new CardSet();
		cardSet.setId(cardSetsCursor.getInt(cardSetsCursor.getColumnIndex("_id")));
		cardSet.setName(cardSetsCursor.getString(cardSetsCursor.getColumnIndex("name")));

		return cardSet;
	}

	private void ensureAtLeastOneFound(int cardSetId, Cursor cardSetsCursor)
			throws NoCardSetFoundException {
		if (!cardSetsCursor.moveToNext()) {
			throw new NoCardSetFoundException(String.format(
					"Card set wasn't found in the database for id [%s]", cardSetId));
		}
	}

	private Cursor queryCardSetByName(String cardSetName) {
		return database.rawQuery(SELECT_CARD_SET_BY_NAME, new String[] { cardSetName });
	}

	private Cursor queryAllCardSets() {
		return database.rawQuery(SELECT_ALL_CARD_SETS, null);
	}

	public void close() {
		database.close();
	}

	public static class NoCardSetFoundException extends Exception {
		private static final long serialVersionUID = -890579994840426052L;

		public NoCardSetFoundException(String message) {
			super(message);
		}
	}
}
