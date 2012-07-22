package org.verba.mobile;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryEntryDao;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public abstract class DictionaryActivity extends VerbaActivity implements ServiceConnection {
	protected DictionaryDao dictionaryDao;
	protected DictionaryEntryDao dictionaryEntryDao;

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		DictionaryBinder binder = (DictionaryBinder) service;
		dictionaryDao = binder.getDictionaryDao();
		dictionaryEntryDao = binder.getDictionaryEntryDao();
		postDictionaryServiceConnected();
	}

	protected void postDictionaryServiceConnected() {
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		dictionaryDao = null;
		dictionaryEntryDao = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = new Intent(this, DictionaryDataService.class);
		bindService(intent, this, BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (dictionaryDao != null || dictionaryEntryDao != null) {
			unbindService(this);
			dictionaryDao = null;
			dictionaryEntryDao = null;
		}
	}
}
