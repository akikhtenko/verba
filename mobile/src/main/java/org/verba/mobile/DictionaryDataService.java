package org.verba.mobile;

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
	private final IBinder mBinder = new DictionaryBinder();

	public class DictionaryBinder extends Binder {
		DictionaryDao getDictionaryDao() {
			return dictionaryDao;
		}

		DictionaryEntryDao getDictionaryEntryDao() {
			return dictionaryEntryDao;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		VerbaDbManager verbaDbManager = new VerbaDbManager(getApplicationContext());
		dictionaryDao = new DictionaryDao(verbaDbManager);
		dictionaryEntryDao = new DictionaryEntryDao(verbaDbManager);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dictionaryDao.close();
		dictionaryEntryDao.close();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}