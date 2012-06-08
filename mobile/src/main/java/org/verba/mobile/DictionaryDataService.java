package org.verba.mobile;

import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDataObject;
import org.verba.mobile.tools.VerbaDbManager;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class DictionaryDataService extends Service {
	private DictionaryDao dictionaryDao;
	private final IBinder mBinder = new DictionaryBinder();

	public class DictionaryBinder extends Binder {
		DictionaryDataService getService() {
			return DictionaryDataService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		VerbaDbManager verbaDbManager = new VerbaDbManager(getApplicationContext());
		dictionaryDao = new DictionaryDao(verbaDbManager);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public DictionaryDataObject getDictionaryByName(String dictionaryName) throws NoDictionaryFoundException,
			MoreThanOneDictionaryFoundException {
		return dictionaryDao.getDictionaryByName(dictionaryName);
	}
}