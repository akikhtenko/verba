package org.verba.mobile;

import java.util.List;

import org.verba.mobile.stardict.DictionaryDao.MoreThanOneDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDao.NoDictionaryFoundException;
import org.verba.mobile.stardict.DictionaryDataObject;
import org.verba.mobile.stardict.DictionaryEntryDataObject;
import org.verba.mobile.task.DictionaryPopulatorTask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PhraseLookupActivity extends DictionaryActivity implements OnClickListener, TextWatcher,
		OnItemClickListener {
	private static final int SUGGESTIONS_LIMIT = 100;
	private static final int DICTIONARY_SIZE = 43041; // 706;
	private EditText phraseToLookupField;
	private ListView phraseSuggestionsList;

	@Override
	public void onClick(View v) {
		lookupPhrase(getPhraseToLookup());
	}

	private String getPhraseToLookup() {
		return phraseToLookupField.getText().toString();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupLookupButton();
		setupPhraseToLookupField();
		setupPhraseSuggestionsList();
	}

	@Override
	protected int getContentLayout() {
		return R.layout.phrase_definition_lookup;
	}

	private void setupPhraseSuggestionsList() {
		phraseSuggestionsList = (ListView) findViewById(R.id.phraseSuggestions);
		phraseSuggestionsList.setOnItemClickListener(this);
	}

	private void setupLookupButton() {
		Button button = (Button) findViewById(R.id.lookupButton);
		button.setOnClickListener(this);
	}

	private void setupPhraseToLookupField() {
		phraseToLookupField = (EditText) findViewById(R.id.wordToFindField);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupRestoreSafePhraseChangeListener();
	}

	private void setupRestoreSafePhraseChangeListener() {
		phraseToLookupField.addTextChangedListener(this);
	}

	@Override
	protected void postDictionaryServiceConnected() {
		checkDictionaryRegistration();
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
		ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//ProgressBar progress = (ProgressBar) findViewById(R.id.dictionaryPopulationProgress);
		new DictionaryPopulatorTask(progressDialog, dictionaryId, DICTIONARY_SIZE, dictionaryEntryDao).execute();
	}

	@Override
	public void afterTextChanged(Editable text) {
		if (text.length() == 0) {
			phraseSuggestionsList.setAdapter(null);
		} else {
			List<DictionaryEntryDataObject> suggestions = dictionaryEntryDao.getTopSuggestions(text.toString(),
					SUGGESTIONS_LIMIT);

			ListAdapter suggestionsListDatasource = new ArrayAdapter<DictionaryEntryDataObject>(this,
					R.layout.list_item, R.id.listItemTitle, suggestions);
			phraseSuggestionsList.setAdapter(suggestionsListDatasource);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence text, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		DictionaryEntryDataObject selectedDictionaryEntry = (DictionaryEntryDataObject) phraseSuggestionsList
				.getItemAtPosition(position);
		lookupPhrase(selectedDictionaryEntry.getPhrase());
	}
}