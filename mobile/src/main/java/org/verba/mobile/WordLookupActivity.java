package org.verba.mobile;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WordLookupActivity extends Activity implements OnClickListener {
	private DictionaryDataService dictionaryDataService;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			DictionaryBinder binder = (DictionaryBinder) service;
			dictionaryDataService = binder.getService();
			checkDictionaryRegistration();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			dictionaryDataService = null;
		}
	};

	@Override
	public void onClick(View v) {
		Intent commandToOpenWordDefinitionDetails = new Intent(this, WordDefinitionDetailsActivity.class);
		commandToOpenWordDefinitionDetails.putExtra("wordToLookup", getWordToLookup());
		startActivity(commandToOpenWordDefinitionDetails);
	}

	private String getWordToLookup() {
		EditText wordToLookupField = (EditText) findViewById(R.id.wordToFindField);
		return wordToLookupField.getText().toString();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button = (Button) findViewById(R.id.lookupButton);
		button.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();

		Intent intent = new Intent(this, DictionaryDataService.class);
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (dictionaryDataService != null) {
			unbindService(serviceConnection);
			dictionaryDataService = null;
		}
	}

	private void checkDictionaryRegistration() {
		try {
			dictionaryDataService.getDictionaryByName("dictionary");
			Log.d("Verba", "Dctionary found");
		} catch (NoDictionaryFoundException e) {
			Log.d("Verba", "Dctionary wasn't found in the database");
		} catch (MoreThanOneDictionaryFoundException e) {
			throw new RuntimeException(e);
		}
	}
}