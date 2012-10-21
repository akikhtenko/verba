package org.verba.mobile.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.mobile.repository.SqliteCardRepository.DELETE_CARD;
import static org.verba.mobile.repository.SqliteCardRepository.INSERT_CARD;
import static org.verba.mobile.repository.SqliteCardRepository.SELECT_CARDS_BY_CARD_SET_ID;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.Card;
import org.verba.mobile.repository.SqliteCardRepository.NoCardFoundException;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(MockitoJUnitRunner.class)
public class SqliteCardRepositoryTest {
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

	private SqliteCardRepository sqliteCardRepository;

	@Before
	public void prepareDbInfrastructure() {
		when(verbaDbManager.getWritableDatabase()).thenReturn(sqLiteDatabase);
		when(sqLiteDatabase.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
		sqliteCardRepository = new SqliteCardRepository(verbaDbManager);
	}

	@Test
	public void shouldAddCard() {
		Card card = new Card();
		card.setPhrase(CARD_PHRASE);
		card.setDefinition(CARD_DEFINITION);
		card.setCardSetId(CARD_SET_ID);

		sqliteCardRepository.addCard(card);

		verify(sqLiteDatabase).execSQL(INSERT_CARD, new Object[] {
				CARD_PHRASE,
				CARD_DEFINITION,
				CARD_SET_ID});
	}

	@Test
	public void shouldDeleteCard() {
		Card card = new Card();
		card.setId(CARD_ID);

		sqliteCardRepository.deleteCard(card);

		verify(sqLiteDatabase).execSQL(DELETE_CARD, new Object[] {CARD_ID});
	}

	@Test
	public void shouldGetCardById() {
		givenOneRowInCursor();

		Card card = sqliteCardRepository.getCardById(CARD_ID);

		assertThat(card.getId(), is(CARD_ID));
		assertThat(card.getCardSetId(), is(CARD_SET_ID));
		assertThat(card.getPhrase(), is(CARD_PHRASE));
		assertThat(card.getDefinition(), is(CARD_DEFINITION));
	}

	@Test
	public void shouldGetCardsInCardSet() {
		givenOneRowInCursor();

		List<Card> cards = sqliteCardRepository.getCardsFromCardSet(CARD_SET_ID, 1);

		verify(sqLiteDatabase).rawQuery(SELECT_CARDS_BY_CARD_SET_ID, new String[] {
				String.valueOf(CARD_SET_ID), String.valueOf(1)});
		Card card = cards.get(0);

		assertThat(card.getId(), is(CARD_ID));
		assertThat(card.getCardSetId(), is(CARD_SET_ID));
		assertThat(card.getPhrase(), is(CARD_PHRASE));
		assertThat(card.getDefinition(), is(CARD_DEFINITION));
	}

	@Test(expected = NoCardFoundException.class)
	public void shouldThrowExceptionWhenCardIsNotFound() {
		givenNoRowsInCursor();

		sqliteCardRepository.getCardById(CARD_ID);
	}

	@Test
	public void shouldCloseDatabase() {
		sqliteCardRepository.close();
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
