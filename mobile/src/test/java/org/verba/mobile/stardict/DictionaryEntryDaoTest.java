package org.verba.mobile.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.mobile.stardict.DictionaryEntryDao.DELETE_DICTIONARY_ENTRY;
import static org.verba.mobile.stardict.DictionaryEntryDao.INSERT_DICTIONARY_ENTRY;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.mobile.stardict.DictionaryEntryDao.NoDictionaryEntryFoundException;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryEntryDaoTest {
	private static final int DICTIONARY_ENTRY_ID = 123;
	private static final int DICTIONARY_ID = 789;
	private static final int DICTIONARY_ENTRY_LENGTH = 456;
	private static final String DICTIONARY_ENTRY_PHRASE = "phrase";
	private static final long DICTIONARY_ENTRY_OFFSET = 321L;

	@Mock
	private VerbaDbManager verbaDbManager;
	@Mock
	private SQLiteDatabase sqLiteDatabase;
	@Mock
	private Cursor cursor;

	private DictionaryEntryDao dictionaryEntryDao;

	@Before
	public void prepareDbInfrastructure() {
		when(verbaDbManager.getWritableDatabase()).thenReturn(sqLiteDatabase);
		when(sqLiteDatabase.rawQuery(anyString(), aryEq(new String[] { String.valueOf(DICTIONARY_ENTRY_ID) })))
		.thenReturn(cursor);
		dictionaryEntryDao = new DictionaryEntryDao(verbaDbManager);
	}

	@Test
	public void shouldAddDictionaryEntry() {
		DictionaryEntryDataObject dictionaryEntryDataObject = new DictionaryEntryDataObject();
		dictionaryEntryDataObject.setPhrase(DICTIONARY_ENTRY_PHRASE);
		dictionaryEntryDataObject.setOffset(DICTIONARY_ENTRY_OFFSET);
		dictionaryEntryDataObject.setLength(DICTIONARY_ENTRY_LENGTH);
		dictionaryEntryDataObject.setDictionaryId(DICTIONARY_ID);

		dictionaryEntryDao.addDictionaryEntry(dictionaryEntryDataObject);

		verify(sqLiteDatabase).execSQL(INSERT_DICTIONARY_ENTRY, new Object[] {
				DICTIONARY_ENTRY_PHRASE,
				DICTIONARY_ENTRY_OFFSET,
				DICTIONARY_ENTRY_LENGTH,
				DICTIONARY_ID});
	}

	@Test
	public void shouldDeleteDictionaryEntry() {
		DictionaryEntryDataObject dictionaryEntryDataObject = new DictionaryEntryDataObject();
		dictionaryEntryDataObject.setId(DICTIONARY_ENTRY_ID);

		dictionaryEntryDao.deleteDictionaryEntry(dictionaryEntryDataObject);

		verify(sqLiteDatabase).execSQL(DELETE_DICTIONARY_ENTRY, new Object[] {DICTIONARY_ENTRY_ID});
	}

	@Test
	public void shouldGetDictionaryEntryById() throws NoDictionaryEntryFoundException {
		givenOneRowInCursor();

		DictionaryEntryDataObject dictionaryEntryDataObject = dictionaryEntryDao.getDictionaryEntryById(DICTIONARY_ENTRY_ID);

		assertThat(dictionaryEntryDataObject.getId(), is(DICTIONARY_ENTRY_ID));
		assertThat(dictionaryEntryDataObject.getDictionaryId(), is(DICTIONARY_ID));
		assertThat(dictionaryEntryDataObject.getOffset(), is(DICTIONARY_ENTRY_OFFSET));
		assertThat(dictionaryEntryDataObject.getLength(), is(DICTIONARY_ENTRY_LENGTH));
		assertThat(dictionaryEntryDataObject.getPhrase(), is(DICTIONARY_ENTRY_PHRASE));
	}

	@Test(expected = NoDictionaryEntryFoundException.class)
	public void shouldThrowExceptionWhenDictionaryEntryIsNotFound() throws NoDictionaryEntryFoundException {
		givenNoRowsInCursor();

		dictionaryEntryDao.getDictionaryEntryById(DICTIONARY_ENTRY_ID);
	}

	@Test
	public void shouldCloseDatabase() {
		dictionaryEntryDao.close();
		verify(sqLiteDatabase).close();
	}

	private void givenNoRowsInCursor() {
		when(cursor.moveToFirst()).thenReturn(false);
	}

	private void givenOneRowInCursor() {
		when(cursor.moveToFirst()).thenReturn(true);
		when(cursor.moveToNext()).thenReturn(false);
		when(cursor.getInt(anyInt())).thenReturn(DICTIONARY_ENTRY_ID, DICTIONARY_ENTRY_LENGTH, DICTIONARY_ID);
		when(cursor.getString(anyInt())).thenReturn(DICTIONARY_ENTRY_PHRASE);
		when(cursor.getLong(anyInt())).thenReturn(DICTIONARY_ENTRY_OFFSET);
	}
}
