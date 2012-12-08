package org.verba.mobile.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.mobile.repository.SqliteCardSetRepository.DELETE_CARD_SET;
import static org.verba.mobile.repository.SqliteCardSetRepository.INSERT_CARD_SET;
import static org.verba.mobile.repository.SqliteCardSetRepository.SELECT_ALL_CARD_SETS;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.CardSet;
import org.verba.mobile.repository.SqliteCardSetRepository.NoCardSetFoundException;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(MockitoJUnitRunner.class)
public class SqliteCardSetRepositoryTest {
	private static final Integer CARD_SET_ID = 123;
	private static final String CARD_SET_NAME = "card set";

	@Mock
	private VerbaDbManager verbaDbManager;
	@Mock
	private SQLiteDatabase sqLiteDatabase;
	@Mock
	private Cursor cursor;

	private SqliteCardSetRepository sqliteCardSetRepository;

	@Before
	public void prepareDbInfrastructure() {
		when(verbaDbManager.getReadableDatabase()).thenReturn(sqLiteDatabase);
		when(verbaDbManager.getWritableDatabase()).thenReturn(sqLiteDatabase);
		when(sqLiteDatabase.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
		sqliteCardSetRepository = new SqliteCardSetRepository(verbaDbManager);
	}

	@Test
	public void shouldAddCardSet() {
		CardSet cardSet = new CardSet();
		cardSet.setName(CARD_SET_NAME);

		givenOneRowInCursor();
		int cardSetId = sqliteCardSetRepository.addCardSet(cardSet);

		verify(sqLiteDatabase).execSQL(INSERT_CARD_SET, new String[] {CARD_SET_NAME});
		assertThat(cardSetId, is(CARD_SET_ID));
	}

	@Test
	public void shouldDeleteCardSet() {
		CardSet cardSet = new CardSet();
		cardSet.setId(CARD_SET_ID);

		sqliteCardSetRepository.deleteCardSet(cardSet);

		verify(sqLiteDatabase).execSQL(DELETE_CARD_SET, new Object[] {CARD_SET_ID});
	}

	@Test
	public void shouldGetCardSetById() throws NoCardSetFoundException {
		givenOneRowInCursor();

		CardSet cardSet = sqliteCardSetRepository.getCardSetById(CARD_SET_ID);

		assertThat(cardSet.getName(), is(CARD_SET_NAME));
	}

	@Test
	public void shouldGetCardsInCardSet() {
		givenOneRowInCursor();

		List<CardSet> cardSets = sqliteCardSetRepository.getAllCardSets();

		verify(sqLiteDatabase).rawQuery(SELECT_ALL_CARD_SETS, null);
		CardSet cardSet = cardSets.get(0);

		assertThat(cardSet.getId(), is(CARD_SET_ID));
		assertThat(cardSet.getName(), is(CARD_SET_NAME));
	}

	@Test(expected = NoCardSetFoundException.class)
	public void shouldThrowExceptionWhenCardSetIsNotFound() throws NoCardSetFoundException {
		givenNoRowsInCursor();

		sqliteCardSetRepository.getCardSetById(CARD_SET_ID);
	}

	@Test
	public void shouldCloseDatabase() {
		sqliteCardSetRepository.close();
		verify(sqLiteDatabase).close();
	}

	private void givenNoRowsInCursor() {
		when(cursor.moveToFirst()).thenReturn(false);
	}

	private void givenOneRowInCursor() {
		when(cursor.moveToNext()).thenReturn(true, false);
		when(cursor.getInt(anyInt())).thenReturn(CARD_SET_ID);
		when(cursor.getString(anyInt())).thenReturn(CARD_SET_NAME);
	}
}
