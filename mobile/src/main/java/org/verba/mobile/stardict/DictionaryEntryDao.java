package org.verba.mobile.stardict;

import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictionaryEntryDao {
	public static final String SELECT_DICTIONARY_ENTRY_BY_ID =
			"select rowid as _id, * from dictionary_entry where rowid = ?";
	public static final String SELECT_DICTIONARY_ENTRY_BY_PATTERN =
			"select rowid as _id, * from dictionary_entry where phrase match ?";
	public static final String INSERT_DICTIONARY_ENTRY = "insert into dictionary_entry "
																		+ "(phrase, offset, length, dictionary_id) "
																		+ "values "
																		+ "(?, ?, ?, ?)";
	public static final String DELETE_DICTIONARY_ENTRY = "delete from dictionary_entry where rowid = ?";

	private SQLiteDatabase database;

	public DictionaryEntryDao(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	public DictionaryEntryDataObject getDictionaryEntryById(int dictionaryEntryId) throws NoDictionaryEntryFoundException {
		Cursor dictionaryEntriesCursor = queryDictionaryEntryById(dictionaryEntryId);

		ensureAtLeastOneFound(dictionaryEntryId, dictionaryEntriesCursor);

		return extractDictionaryEntryFromCursor(dictionaryEntriesCursor);
	}

	public DictionaryEntryDataObject getDictionaryEntryByPhrasePattern(String pattern)
			throws NoDictionaryEntryFoundException {
		Cursor dictionaryEntriesCursor = queryDictionaryEntryByPhrasePattern(pattern);

		ensureAtLeastOneFound(pattern, dictionaryEntriesCursor);

		return extractDictionaryEntryFromCursor(dictionaryEntriesCursor);
	}

	public void addDictionaryEntry(DictionaryEntryDataObject dictionaryEntryDataObject) {
		database.execSQL(INSERT_DICTIONARY_ENTRY,
				new Object[] {
							dictionaryEntryDataObject.getPhrase(),
							dictionaryEntryDataObject.getOffset(),
							dictionaryEntryDataObject.getLength(),
							dictionaryEntryDataObject.getDictionaryId()});
	}

	public void deleteDictionaryEntry(DictionaryEntryDataObject dictionaryEntryDataObject) {
		database.execSQL(DELETE_DICTIONARY_ENTRY, new Object[] { dictionaryEntryDataObject.getId() });
	}

	private DictionaryEntryDataObject extractDictionaryEntryFromCursor(Cursor cursor) {
		DictionaryEntryDataObject dictionaryEntry = new DictionaryEntryDataObject();
		dictionaryEntry.setId(cursor.getInt(cursor.getColumnIndex("_id")));
		dictionaryEntry.setPhrase(cursor.getString(cursor.getColumnIndex("phrase")));
		dictionaryEntry.setOffset(cursor.getLong(cursor.getColumnIndex("offset")));
		dictionaryEntry.setLength(cursor.getInt(cursor.getColumnIndex("length")));
		dictionaryEntry.setDictionaryId(cursor.getInt(cursor.getColumnIndex("dictionary_id")));

		return dictionaryEntry;
	}

	private void ensureAtLeastOneFound(int dictionaryEntryId, Cursor dictionaryEntriesCursor)
			throws NoDictionaryEntryFoundException {
		if (!dictionaryEntriesCursor.moveToFirst()) {
			throw new NoDictionaryEntryFoundException(String.format(
					"Dictionary entry wasn't found in the database for id [%s]", dictionaryEntryId));
		}
	}

	private void ensureAtLeastOneFound(String pattern, Cursor dictionaryEntriesCursor)
			throws NoDictionaryEntryFoundException {
		if (!dictionaryEntriesCursor.moveToFirst()) {
			throw new NoDictionaryEntryFoundException(String.format(
					"Dictionary entry wasn't found in the database by phrase pattern [%s]", pattern));
		}
	}

	private Cursor queryDictionaryEntryById(int dictionaryEntryId) {
		return database.rawQuery(SELECT_DICTIONARY_ENTRY_BY_ID, new String[] { String.valueOf(dictionaryEntryId) });
	}

	private Cursor queryDictionaryEntryByPhrasePattern(String pattern) {
		return database.rawQuery(SELECT_DICTIONARY_ENTRY_BY_PATTERN, new String[] { pattern });
	}

	public static class NoDictionaryEntryFoundException extends Exception {
		private static final long serialVersionUID = 7715190734536758220L;

		public NoDictionaryEntryFoundException(String message) {
			super(message);
		}
	}

	public void close() {
		database.close();
	}

	public void doInTransaction(DoInTransactionCallback callback) throws Exception {
		database.beginTransaction();
		try {
			callback.doInTransaction();
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	public interface DoInTransactionCallback {
		void doInTransaction() throws Exception;
	}
}
