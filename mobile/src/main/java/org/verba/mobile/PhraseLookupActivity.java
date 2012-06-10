package org.verba.mobile;

import org.verba.mobile.DictionaryDataService.DictionaryBinder;
import org.verba.mobile.stardict.DictionaryDao;
import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDataObject;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.task.DictionaryPopulatorTask;
import org.verba.mobile.task.LookupPhraseDefinitionCoordinatesTask;
import org.verba.stardict.PhraseDefinitionCoordinates;

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
import android.widget.ProgressBar;
import android.widget.Toast;

public class PhraseLookupActivity extends Activity implements OnClickListener, ServiceConnection {
	private static final int DICTIONARY_SIZE = 43041; // 706;
	private DictionaryDao dictionaryDao;
	private DictionaryEntryDao dictionaryEntryDao;

	@Override
	public void onClick(View v) {
		new LookupPhraseDefinitionCoordinatesTask(this, dictionaryEntryDao).execute(getPhraseToLookup());
	}

	public void displayPhraseDefinitionNotFound() {
		Toast.makeText(getApplicationContext(), "Nothing found in the dictionary!", Toast.LENGTH_SHORT).show();
	}

	public void displayPhraseDefinition(PhraseDefinitionCoordinates phraseDefinitionCoordinates) {
		Intent commandToOpenPhraseDefinitionDetails = new Intent(this, PhraseDefinitionDetailsActivity.class);
		commandToOpenPhraseDefinitionDetails.putExtra("wordToLookup", phraseDefinitionCoordinates);
		startActivity(commandToOpenPhraseDefinitionDetails);
	}

	private String getPhraseToLookup() {
		EditText phraseToLookupField = (EditText) findViewById(R.id.wordToFindField);
		return phraseToLookupField.getText().toString();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button button = (Button) findViewById(R.id.lookupButton);
		button.setOnClickListener(this);
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

	@Override
	public void onServiceConnected(ComponentName className, IBinder service) {
		DictionaryBinder binder = (DictionaryBinder) service;
		dictionaryDao = binder.getDictionaryDao();
		dictionaryEntryDao = binder.getDictionaryEntryDao();
		checkDictionaryRegistration();
	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		dictionaryDao = null;
		dictionaryEntryDao = null;
	}

	private void checkDictionaryRegistration() {
		try {
			DictionaryDataObject dictionaryDataObject = dictionaryDao.getDictionaryByName("dictionary");
			Log.d("Verba", "Dictionary found");
//			dictionaryDao.deleteDictionary(dictionaryDataObject);
//			Log.d("Verba", "Dictionary deleted");
		} catch (NoDictionaryFoundException e) {
			Log.d("Verba", "Dictionary wasn't found in the database");

			addNewDictionary();
		} catch (MoreThanOneDictionaryFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void addNewDictionary() {
		DictionaryDataObject dictionaryDataObject = new DictionaryDataObject();
		dictionaryDataObject.setName("dictionary");
		dictionaryDataObject.setDescription("dictionary description");

		int dictionaryId = dictionaryDao.addDictionary(dictionaryDataObject);
		populateDictionary(dictionaryId);
	}

	private void populateDictionary(int dictionaryId) {
		ProgressBar progress = (ProgressBar) findViewById(R.id.dictionaryPopulationProgress);
		new DictionaryPopulatorTask(progress, dictionaryId, DICTIONARY_SIZE, dictionaryEntryDao).execute();
	}
}