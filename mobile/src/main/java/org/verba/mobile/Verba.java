package org.verba.mobile;

import java.io.File;

import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSetDao;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.tools.VerbaDbManager;

import android.app.Application;
import android.os.Environment;

public class Verba extends Application {
	private DictionaryDao dictionaryDao;
	private DictionaryEntryDao dictionaryEntryDao;
	private CardSetDao cardSetDao;
	private CardDao cardDao;

	@Override
	public void onCreate() {
		super.onCreate();

		VerbaDbManager verbaDbManager = new VerbaDbManager(getApplicationContext());
		dictionaryDao = new DictionaryDao(verbaDbManager);
		dictionaryEntryDao = new DictionaryEntryDao(verbaDbManager);
		cardSetDao = new CardSetDao(verbaDbManager);
		cardDao = new CardDao(verbaDbManager);
	}

	@Override
	public void onTerminate() {
		dictionaryDao.close();
		dictionaryEntryDao.close();
		cardSetDao.close();
		cardDao.close();

		super.onTerminate();
	};

	public DictionaryDao getDictionaryDao() {
		return dictionaryDao;
	}

	public DictionaryEntryDao getDictionaryEntryDao() {
		return dictionaryEntryDao;
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
