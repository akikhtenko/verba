package org.verba.mobile.repository;

import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryDataObject;
import org.verba.DictionaryRepository;
import org.verba.mobile.tools.VerbaDbManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDictionaryRepository implements DictionaryRepository {
	public static final String SELECT_ALL_DICTIONARIES = "select * from dictionary order by position";
	public static final String SELECT_DICTIONARY_BY_NAME = "select * from dictionary where name = ?";
	public static final String SELECT_DICTIONARY_BY_ID = "select * from dictionary where _id = ?";
	public static final String INSERT_DICTIONARY = "insert into dictionary (name, description, position) "
			+ "values (?, ?, (SELECT IFNULL(MAX(position)+1, 1) FROM dictionary))";
	public static final String DELETE_DICTIONARY = "delete from dictionary where _id = ?";
	public static final String SHIFT_POSITIONS_UP = "update dictionary set position = position-1 where position > ? and position <= ?";
	public static final String SHIFT_POSITIONS_DOWN = "update dictionary set position = position+1 where position >= ? and position < ?";
	public static final String ASSIGN_NEW_POSITION = "update dictionary set position = ? where _id = ?";

	private SQLiteDatabase database;

	public SqliteDictionaryRepository(VerbaDbManager aVerbaDbManager) {
		database = aVerbaDbManager.getWritableDatabase();
	}

	@Override
	public List<DictionaryDataObject> getAllDictionaries() {
		Cursor dictionariesCursor = queryAllDictionaries();
		return extractDictionariesFromCursor(dictionariesCursor);
	}

	public DictionaryDataObject getDictionaryByName(String dictionaryName) {
		Cursor dictionariesCursor = queryDictionaryByName(dictionaryName);

		ensureAtLeastOneFound(dictionaryName, dictionariesCursor);

		DictionaryDataObject result = extractDictionaryFromCursor(dictionariesCursor);

		ensureOnlyOneFound(dictionaryName, dictionariesCursor);

		return result;
	}

	@Override
	public DictionaryDataObject getDictionaryById(int dictionaryId) {
		Cursor dictionariesCursor = queryDictionaryById(dictionaryId);

		ensureAtLeastOneFound(dictionaryId, dictionariesCursor);

		DictionaryDataObject result = extractDictionaryFromCursor(dictionariesCursor);

		ensureOnlyOneFound(dictionaryId, dictionariesCursor);

		return result;
	}

	@Override
	public boolean exists(String dictionaryName) {
		Cursor dictionariesCursor = queryDictionaryByName(dictionaryName);

		return dictionariesCursor.moveToFirst();
	}

	@Override
	public int addDictionary(DictionaryDataObject dictionaryDataObject) {
		database.execSQL(INSERT_DICTIONARY,
				new String[] { dictionaryDataObject.getName(), dictionaryDataObject.getDescription() });

		return getInsertedDictionaryId(dictionaryDataObject);
	}

	@Override
	public void moveToPosition(DictionaryDataObject dictionaryDataObject, int position) {
		database.beginTransaction();
		try {
			doMoveToPosition(dictionaryDataObject, position);
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}

	private void doMoveToPosition(DictionaryDataObject dictionaryDataObject, int newPosition) {
		if (dictionaryDataObject.getPosition() == newPosition) {
			return;
		}

		if (newPosition > dictionaryDataObject.getPosition()) {
			shiftUpAllDictionariesBetween(dictionaryDataObject.getPosition(), newPosition);
		} else {
			shiftDownAllDictionariesBetween(newPosition, dictionaryDataObject.getPosition());
		}
		assignNewPosition(dictionaryDataObject, newPosition);
	}

	private void shiftUpAllDictionariesBetween(int newPosition, int oldPosition) {
		database.execSQL(SHIFT_POSITIONS_UP, new Object[] { newPosition, oldPosition });
	}

	private void shiftDownAllDictionariesBetween(int oldPosition, int newPosition) {
		database.execSQL(SHIFT_POSITIONS_DOWN, new Object[] { oldPosition, newPosition });
	}

	private void assignNewPosition(DictionaryDataObject dictionaryDataObject, int newPosition) {
		database.execSQL(ASSIGN_NEW_POSITION, new Object[] { newPosition, dictionaryDataObject.getId() });
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

	private List<DictionaryDataObject> extractDictionariesFromCursor(Cursor cursor) {
		List<DictionaryDataObject> result = new ArrayList<DictionaryDataObject>();
		while (cursor.moveToNext()) {
			result.add(extractDictionaryFromCursor(cursor));
		}
		return result;
	}

	private DictionaryDataObject extractDictionaryFromCursor(Cursor dictionariesCursor) {
		DictionaryDataObject dictionary = new DictionaryDataObject();
		dictionary.setId(dictionariesCursor.getInt(dictionariesCursor.getColumnIndex("_id")));
		dictionary.setName(dictionariesCursor.getString(dictionariesCursor.getColumnIndex("name")));
		dictionary.setDescription(dictionariesCursor.getString(dictionariesCursor.getColumnIndex("description")));
		dictionary.setPosition(dictionariesCursor.getInt(dictionariesCursor.getColumnIndex("position")));

		return dictionary;
	}

	private void ensureAtLeastOneFound(String dictionaryName, Cursor dictionariesCursor) {
		if (!dictionariesCursor.moveToFirst()) {
			throw new NoDictionaryFoundException(String.format(
					"Dictionary wasn't found in the database for the name [%s]", dictionaryName));
		}
	}

	private void ensureAtLeastOneFound(int dictionaryId, Cursor dictionariesCursor) {
		if (!dictionariesCursor.moveToFirst()) {
			throw new NoDictionaryFoundException(String.format(
					"Dictionary wasn't found in the database for the id [%s]", dictionaryId));
		}
	}

	private void ensureOnlyOneFound(String dictionaryName, Cursor dictionariesCursor) {
		if (dictionariesCursor.moveToNext()) {
			throw new MoreThanOneDictionaryFoundException(String.format(
					"More than one dictionary was found in the database for the name [%s]", dictionaryName));
		}
	}

	private void ensureOnlyOneFound(int dictionaryId, Cursor dictionariesCursor) {
		if (dictionariesCursor.moveToNext()) {
			throw new MoreThanOneDictionaryFoundException(String.format(
					"More than one dictionary was found in the database for the id [%s]", dictionaryId));
		}
	}

	private Cursor queryDictionaryByName(String dictionaryName) {
		return database.rawQuery(SELECT_DICTIONARY_BY_NAME, new String[] { dictionaryName });
	}

	private Cursor queryDictionaryById(int dictionaryId) {
		return database.rawQuery(SELECT_DICTIONARY_BY_ID, new String[] { String.valueOf(dictionaryId) });
	}

	private Cursor queryAllDictionaries() {
		return database.rawQuery(SELECT_ALL_DICTIONARIES, null);
	}

	public static class NoDictionaryFoundException extends RuntimeException {
		private static final long serialVersionUID = -4857208043479570757L;

		public NoDictionaryFoundException(String message) {
			super(message);
		}
	}

	public static class MoreThanOneDictionaryFoundException extends RuntimeException {
		private static final long serialVersionUID = -2213808607247745721L;

		public MoreThanOneDictionaryFoundException(String message) {
			super(message);
		}
	}

	public void close() {
		database.close();
	}
}
