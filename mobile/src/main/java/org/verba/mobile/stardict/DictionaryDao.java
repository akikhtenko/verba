package org.verba.mobile.stardict;

import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictionaryDao {
	public static final String SELECT_DICTIONARY_BY_NAME = "select * from dictionary where name = ?";
	public static final String INSERT_DICTIONARY = "insert into dictionary (name, description) values (?, ?)";
	public static final String DELETE_DICTIONARY = "delete from dictionary where _id = ?";

	private SQLiteDatabase database;

	public DictionaryDao(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	public DictionaryDataObject getDictionaryByName(String dictionaryName) throws NoDictionaryFoundException,
			MoreThanOneDictionaryFoundException {
		Cursor dictionariesCursor = queryDictionaryByName(dictionaryName);

		ensureAtLeastOneFound(dictionaryName, dictionariesCursor);

		DictionaryDataObject result = extractDictionaryFromCursor(dictionariesCursor);

		ensureOnlyOneFound(dictionaryName, dictionariesCursor);

		return result;
	}

	public int addDictionary(DictionaryDataObject dictionaryDataObject) {
		database.execSQL(INSERT_DICTIONARY,
				new String[] { dictionaryDataObject.getName(), dictionaryDataObject.getDescription() });

		return getInsertedDictionaryId(dictionaryDataObject);
	}

	private int getInsertedDictionaryId(DictionaryDataObject dictionaryDataObject) {
		Cursor dictionaryCursor = queryDictionaryByName(dictionaryDataObject.getName());

		if (dictionaryCursor.moveToFirst()) {
			return dictionaryCursor.getInt(dictionaryCursor.getColumnIndex("_id"));
		} else {
			return -1;
		}
	}

	public void deleteDictionary(DictionaryDataObject dictionaryDataObject) {
		database.execSQL(DELETE_DICTIONARY, new Object[] { dictionaryDataObject.getId() });
	}

	private void ensureOnlyOneFound(String dictionaryName, Cursor dictionariesCursor)
			throws MoreThanOneDictionaryFoundException {
		if (dictionariesCursor.moveToNext()) {
			throw new MoreThanOneDictionaryFoundException(String.format(
					"More than one dictionary was found in the database for the name [%s]", dictionaryName));
		}
	}

	private DictionaryDataObject extractDictionaryFromCursor(Cursor dictionariesCursor) {
		DictionaryDataObject dictionary = new DictionaryDataObject();
		dictionary.setId(dictionariesCursor.getInt(dictionariesCursor.getColumnIndex("_id")));
		dictionary.setName(dictionariesCursor.getString(dictionariesCursor.getColumnIndex("name")));
		dictionary.setDescription(dictionariesCursor.getString(dictionariesCursor.getColumnIndex("description")));

		return dictionary;
	}

	private void ensureAtLeastOneFound(String dictionaryName, Cursor dictionariesCursor)
			throws NoDictionaryFoundException {
		if (!dictionariesCursor.moveToFirst()) {
			throw new NoDictionaryFoundException(String.format(
					"Dictionary wasn't found in the database for the name [%s]", dictionaryName));
		}
	}

	private Cursor queryDictionaryByName(String dictionaryName) {
		return database.rawQuery(SELECT_DICTIONARY_BY_NAME, new String[] { dictionaryName });
	}

	public static class NoDictionaryFoundException extends Exception {
		private static final long serialVersionUID = -4857208043479570757L;

		public NoDictionaryFoundException(String message) {
			super(message);
		}
	}

	public static class MoreThanOneDictionaryFoundException extends Exception {
		private static final long serialVersionUID = -2213808607247745721L;

		public MoreThanOneDictionaryFoundException(String message) {
			super(message);
		}
	}

	public void close() {
		database.close();
	}
}