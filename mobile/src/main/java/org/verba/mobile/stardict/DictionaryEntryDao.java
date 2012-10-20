package org.verba.mobile.stardict;

import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DictionaryEntryDao implements DictionaryEntryRepository {
	private static final String WILDCARD = "*";
	public static final String SELECT_DICTIONARY_ENTRY_BY_ID =
			"select rowid as _id, * from dictionary_entry where rowid = ?";
	public static final String SELECT_DICTIONARY_ENTRIES_BY_PHRASE =
			"select rowid as _id, * from dictionary_entry where phrase = ?";
	public static final String INSERT_DICTIONARY_ENTRY = "insert into dictionary_entry "
																		+ "(phrase, offset, length, dictionary_id) "
																		+ "values "
																		+ "(?, ?, ?, ?)";
	public static final String DELETE_DICTIONARY_ENTRY = "delete from dictionary_entry where rowid = ?";
	public static final String SELECT_PHRASE_SUGGESTIONS =
			"select rowid as _id, * from dictionary_entry where phrase glob ? limit ?";

	private SQLiteDatabase database;

	public DictionaryEntryDao(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	public DictionaryEntryDataObject getDictionaryEntryById(int dictionaryEntryId) throws NoDictionaryEntryFoundException {
		Cursor dictionaryEntriesCursor = queryDictionaryEntryById(dictionaryEntryId);

		ensureAtLeastOneFound(dictionaryEntryId, dictionaryEntriesCursor);

		return extractDictionaryEntryFromCursor(dictionaryEntriesCursor);
	}

	public List<DictionaryEntryDataObject> getDictionaryEntriesByPhrase(String phrase)
			throws NoDictionaryEntryFoundException {
		Cursor dictionaryEntriesCursor = queryDictionaryEntryByPhrase(phrase);

		List<DictionaryEntryDataObject> dictionaryEntries = extractDictionaryEntriesFromCursor(dictionaryEntriesCursor);
		ensureAtLeastOneFound(phrase, dictionaryEntries);

		return dictionaryEntries;
	}

	public List<DictionaryEntryDataObject> getTopSuggestions(String pattern, int count) {
		Cursor dictionaryEntriesCursor = querySuggestionsByPhrasePattern(pattern + WILDCARD, count);

		return extractDictionaryEntriesFromCursor(dictionaryEntriesCursor);
	}

	@Override
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

	private List<DictionaryEntryDataObject> extractDictionaryEntriesFromCursor(Cursor cursor) {
		List<DictionaryEntryDataObject> result = new ArrayList<DictionaryEntryDataObject>();
		while (cursor.moveToNext()) {
			result.add(extractDictionaryEntryFromCursor(cursor));
		}
		return result;
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

	private void ensureAtLeastOneFound(String phrase, List<DictionaryEntryDataObject> dictionaryEntries)
			throws NoDictionaryEntryFoundException {
		if (dictionaryEntries.isEmpty()) {
			throw new NoDictionaryEntryFoundException(String.format(
					"Dictionary entry wasn't found in the database by phrase pattern [%s]", phrase));
		}
	}

	private Cursor queryDictionaryEntryById(int dictionaryEntryId) {
		return database.rawQuery(SELECT_DICTIONARY_ENTRY_BY_ID, new String[] { String.valueOf(dictionaryEntryId) });
	}

	private Cursor queryDictionaryEntryByPhrase(String phrase) {
		return database.rawQuery(SELECT_DICTIONARY_ENTRIES_BY_PHRASE, new String[] { phrase });
	}

	private Cursor querySuggestionsByPhrasePattern(String pattern, int count) {
		return database.rawQuery(SELECT_PHRASE_SUGGESTIONS, new String[] { pattern, String.valueOf(count) });
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

	@Override
	public void doInOneGo(Operation operation) throws Exception {
		database.beginTransaction();
		try {
			operation.doInOneGo();
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
}
