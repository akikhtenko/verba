package org.verba.mobile.card;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.mobile.card.CardDao.DELETE_CARD;
import static org.verba.mobile.card.CardDao.INSERT_CARD;
import static org.verba.mobile.card.CardDao.SELECT_CARDS_BY_CARD_SET_ID;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.mobile.card.CardDao.NoCardFoundException;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(MockitoJUnitRunner.class)
public class CardDaoTest {
	private static final int CARD_ID = 123;
	private static final int CARD_SET_ID = 789;
	private static final String CARD_PHRASE = "phrase";
	private static final String CARD_DEFINITION = "definition";

	@Mock
	private VerbaDbManager verbaDbManager;
	@Mock
	private SQLiteDatabase sqLiteDatabase;
	@Mock
	private Cursor cursor;

	private CardDao cardDao;

	@Before
	public void prepareDbInfrastructure() {
		when(verbaDbManager.getWritableDatabase()).thenReturn(sqLiteDatabase);
		when(sqLiteDatabase.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
		cardDao = new CardDao(verbaDbManager);
	}

	@Test
	public void shouldAddCard() {
		Card card = new Card();
		card.setPhrase(CARD_PHRASE);
		card.setDefinition(CARD_DEFINITION);
		card.setCardSetId(CARD_SET_ID);

		cardDao.addCard(card);

		verify(sqLiteDatabase).execSQL(INSERT_CARD, new Object[] {
				CARD_PHRASE,
				CARD_DEFINITION,
				CARD_SET_ID});
	}

	@Test
	public void shouldDeleteCard() {
		Card card = new Card();
		card.setId(CARD_ID);

		cardDao.deleteCard(card);

		verify(sqLiteDatabase).execSQL(DELETE_CARD, new Object[] {CARD_ID});
	}

	@Test
	public void shouldGetCardById() throws NoCardFoundException {
		givenOneRowInCursor();

		Card card = cardDao.getCardById(CARD_ID);

		assertThat(card.getId(), is(CARD_ID));
		assertThat(card.getCardSetId(), is(CARD_SET_ID));
		assertThat(card.getPhrase(), is(CARD_PHRASE));
		assertThat(card.getDefinition(), is(CARD_DEFINITION));
	}

	@Test
	public void shouldGetCardsInCardSet() throws NoCardFoundException {
		givenOneRowInCursor();

		List<Card> cards = cardDao.getCardsInCardSet(CARD_SET_ID, 1);

		verify(sqLiteDatabase).rawQuery(SELECT_CARDS_BY_CARD_SET_ID, new String[] {
				String.valueOf(CARD_SET_ID), String.valueOf(1)});
		Card card = cards.get(0);

		assertThat(card.getId(), is(CARD_ID));
		assertThat(card.getCardSetId(), is(CARD_SET_ID));
		assertThat(card.getPhrase(), is(CARD_PHRASE));
		assertThat(card.getDefinition(), is(CARD_DEFINITION));
	}

	@Test(expected = NoCardFoundException.class)
	public void shouldThrowExceptionWhenCardIsNotFound() throws NoCardFoundException {
		givenNoRowsInCursor();

		cardDao.getCardById(CARD_ID);
	}

	@Test
	public void shouldCloseDatabase() {
		cardDao.close();
		verify(sqLiteDatabase).close();
	}

	private void givenNoRowsInCursor() {
		when(cursor.moveToFirst()).thenReturn(false);
	}

	private void givenOneRowInCursor() {
		when(cursor.moveToNext()).thenReturn(true, false);
		when(cursor.getInt(anyInt())).thenReturn(CARD_ID, CARD_SET_ID);
		when(cursor.getString(anyInt())).thenReturn(CARD_PHRASE, CARD_DEFINITION);
	}
}
