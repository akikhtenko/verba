package org.verba.mobile;

import java.io.File;

import org.verba.DictionaryEntryRepository;
import org.verba.DictionaryRepository;
import org.verba.card.CardRepository;
import org.verba.cardset.CardSetRepository;
import org.verba.mobile.repository.SqliteCardRepository;
import org.verba.mobile.repository.SqliteCardSetRepository;
import org.verba.mobile.repository.SqliteDictionaryEntryRepository;
import org.verba.mobile.repository.SqliteDictionaryRepository;
import org.verba.mobile.tools.VerbaDbManager;

import android.app.Application;
import android.os.Environment;

public class Verba extends Application {
	private SqliteDictionaryRepository sqliteDictionaryRepository;
	private SqliteDictionaryEntryRepository sqliteDictionaryEntryRepository;
	private SqliteCardSetRepository sqliteCardSetRepository;
	private SqliteCardRepository sqliteCardRepository;

	@Override
	public void onCreate() {
		super.onCreate();

		VerbaDbManager verbaDbManager = new VerbaDbManager(getApplicationContext());
		sqliteDictionaryRepository = new SqliteDictionaryRepository(verbaDbManager);
		sqliteDictionaryEntryRepository = new SqliteDictionaryEntryRepository(verbaDbManager);
		sqliteCardSetRepository = new SqliteCardSetRepository(verbaDbManager);
		sqliteCardRepository = new SqliteCardRepository(verbaDbManager);
	}

	@Override
	public void onTerminate() {
		sqliteDictionaryRepository.close();
		sqliteDictionaryEntryRepository.close();
		sqliteCardSetRepository.close();
		sqliteCardRepository.close();

		super.onTerminate();
	};

	public DictionaryRepository getDictionaryRepository() {
		return sqliteDictionaryRepository;
	}

	public DictionaryEntryRepository getDictionaryEntryRepository() {
		return sqliteDictionaryEntryRepository;
	}

	public CardSetRepository getCardSetRepository() {
		return sqliteCardSetRepository;
	}

	public CardRepository getCardRepository() {
		return sqliteCardRepository;
	}

	public static File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}

}
