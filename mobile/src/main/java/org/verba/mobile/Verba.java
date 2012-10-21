package org.verba.mobile;

import java.io.File;

import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSetDao;
import org.verba.mobile.repository.SqliteDictionaryRepository;
import org.verba.mobile.repository.SqliteDictionaryEntryRepository;
import org.verba.mobile.tools.VerbaDbManager;

import android.app.Application;
import android.os.Environment;

public class Verba extends Application {
	private SqliteDictionaryRepository sqliteDictionaryRepository;
	private SqliteDictionaryEntryRepository sqliteDictionaryEntryRepository;
	private CardSetDao cardSetDao;
	private CardDao cardDao;

	@Override
	public void onCreate() {
		super.onCreate();

		VerbaDbManager verbaDbManager = new VerbaDbManager(getApplicationContext());
		sqliteDictionaryRepository = new SqliteDictionaryRepository(verbaDbManager);
		sqliteDictionaryEntryRepository = new SqliteDictionaryEntryRepository(verbaDbManager);
		cardSetDao = new CardSetDao(verbaDbManager);
		cardDao = new CardDao(verbaDbManager);
	}

	@Override
	public void onTerminate() {
		sqliteDictionaryRepository.close();
		sqliteDictionaryEntryRepository.close();
		cardSetDao.close();
		cardDao.close();

		super.onTerminate();
	};

	public SqliteDictionaryRepository getDictionaryRepository() {
		return sqliteDictionaryRepository;
	}

	public SqliteDictionaryEntryRepository getDictionaryEntryRepository() {
		return sqliteDictionaryEntryRepository;
	}

	public CardSetDao getCardSetDao() {
		return cardSetDao;
	}

	public CardDao getCardDao() {
		return cardDao;
	}

	public static File getVerbaDirectory() {
		return Environment.getExternalStoragePublicDirectory("verba");
	}

}
