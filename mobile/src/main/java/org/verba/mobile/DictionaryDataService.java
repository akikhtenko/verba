package org.verba.mobile;

import org.verba.mobile.card.CardDao;
import org.verba.mobile.card.CardSetDao;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.tools.VerbaDbManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DictionaryDataService extends Service {
	private DictionaryDao dictionaryDao;
	private DictionaryEntryDao dictionaryEntryDao;
	private CardSetDao cardSetDao;
	private CardDao cardDao;
	private final IBinder mBinder = new DictionaryBinder();

	public class DictionaryBinder extends Binder {
		DictionaryDao getDictionaryDao() {
			return dictionaryDao;
		}

		DictionaryEntryDao getDictionaryEntryDao() {
			return dictionaryEntryDao;
		}

		CardSetDao getCardSetDao() {
			return cardSetDao;
		}

		CardDao getCardDao() {
			return cardDao;
		}
	}

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
	public void onDestroy() {
		super.onDestroy();
		dictionaryDao.close();
		dictionaryEntryDao.close();
		cardSetDao.close();
		cardDao.close();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}