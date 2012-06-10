package org.verba.mobile.stardict;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.verba.mobile.stardict.DictionaryDao.DELETE_DICTIONARY;
import static org.verba.mobile.stardict.DictionaryDao.INSERT_DICTIONARY;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

@RunWith(MockitoJUnitRunner.class)
public class DictionaryDaoTest {
	private static final String DICTIONARY_NAME = "dictionary";
	private static final Integer DICTIONARY_ID = 123;
	private static final String DICTIONARY_DESCRIPTION = "description";

	@Mock
	private VerbaDbManager verbaDbManager;
	@Mock
	private SQLiteDatabase sqLiteDatabase;
	@Mock
	private Cursor cursor;

	private DictionaryDao dictionaryDao;

	@Before
	public void prepareDbInfrastructure() {
		when(verbaDbManager.getReadableDatabase()).thenReturn(sqLiteDatabase);
		when(verbaDbManager.getWritableDatabase()).thenReturn(sqLiteDatabase);
		when(sqLiteDatabase.rawQuery(anyString(), aryEq(new String[] {DICTIONARY_NAME}))).thenReturn(cursor);
		dictionaryDao = new DictionaryDao(verbaDbManager);
	}

	@Test
	public void shouldAddDictionary() {
		DictionaryDataObject dictionaryDataObject = new DictionaryDataObject();
		dictionaryDataObject.setName(DICTIONARY_NAME);
		dictionaryDataObject.setDescription(DICTIONARY_DESCRIPTION);

		givenOneRowInCursor();
		int dictionaryId = dictionaryDao.addDictionary(dictionaryDataObject);

		verify(sqLiteDatabase).execSQL(INSERT_DICTIONARY, new String[] {DICTIONARY_NAME, DICTIONARY_DESCRIPTION});
		assertThat(dictionaryId, is(DICTIONARY_ID));
	}

	@Test
	public void shouldDeleteDictionary() {
		DictionaryDataObject dictionaryDataObject = new DictionaryDataObject();
		dictionaryDataObject.setId(DICTIONARY_ID);

		dictionaryDao.deleteDictionary(dictionaryDataObject);

		verify(sqLiteDatabase).execSQL(DELETE_DICTIONARY, new Object[] {DICTIONARY_ID});
	}

	@Test
	public void shouldGetDictionaryByName() throws NoDictionaryFoundException, MoreThanOneDictionaryFoundException {
		givenOneRowInCursor();

		DictionaryDataObject dictionary = dictionaryDao.getDictionaryByName(DICTIONARY_NAME);

		assertThat(dictionary.getName(), is(DICTIONARY_NAME));
	}

	@Test(expected = NoDictionaryFoundException.class)
	public void shouldThrowExceptionWhenDictionaryIsNotFound() throws NoDictionaryFoundException,
			MoreThanOneDictionaryFoundException {
		givenNoRowsInCursor();

		dictionaryDao.getDictionaryByName(DICTIONARY_NAME);
	}

	@Test(expected = MoreThanOneDictionaryFoundException.class)
	public void shouldThrowExceptionWhenMoreThanOneDictionaryFound() throws NoDictionaryFoundException,
			MoreThanOneDictionaryFoundException {
		givenTwoRowsInCursor();

		dictionaryDao.getDictionaryByName(DICTIONARY_NAME);
	}

	@Test
	public void shouldCloseDatabase() {
		dictionaryDao.close();
		verify(sqLiteDatabase).close();
	}

	private void givenNoRowsInCursor() {
		when(cursor.moveToFirst()).thenReturn(false);
	}

	private void givenOneRowInCursor() {
		when(cursor.moveToFirst()).thenReturn(true);
		when(cursor.moveToNext()).thenReturn(false);
		when(cursor.getInt(anyInt())).thenReturn(DICTIONARY_ID);
		when(cursor.getString(anyInt())).thenReturn(DICTIONARY_NAME, DICTIONARY_DESCRIPTION);
	}

	private void givenTwoRowsInCursor() {
		when(cursor.moveToFirst()).thenReturn(true);
		when(cursor.moveToNext()).thenReturn(true, false);
		when(cursor.getInt(anyInt())).thenReturn(DICTIONARY_ID);
		when(cursor.getString(anyInt())).thenReturn(DICTIONARY_NAME, DICTIONARY_DESCRIPTION);
	}
}
