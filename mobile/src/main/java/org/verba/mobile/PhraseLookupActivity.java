package org.verba.mobile;

import static org.verba.mobile.Application.getVerbaDirectory;
import static org.verba.mobile.PhraseDefinitionDetailsActivity.PHRASE_TO_LOOKUP;

import java.util.ArrayList;
import java.util.List;

import org.verba.mobile.dictionary.DictionariesManager;
import org.verba.mobile.stardict.DictionaryEntryDataObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
	public static final String NEW_DICTIONARIES = "newDictionaries";
	private static final int SUGGESTIONS_LIMIT = 100;
	private EditText phraseToLookupField;
	private ListView phraseSuggestionsList;

	@Override
	public void onClick(View v) {
		lookupPhrase(getPhraseToLookup());
	}

	private void lookupPhrase(String phraseToLookup) {
		Intent commandToOpenPhraseDefinitionDetails = new Intent(this, PhraseDefinitionDetailsActivity.class);
		commandToOpenPhraseDefinitionDetails.putExtra(PHRASE_TO_LOOKUP, phraseToLookup);
		startActivity(commandToOpenPhraseDefinitionDetails);
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
	protected boolean loadSystemMenu() {
		return true;
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
		DictionariesManager dictionariesManager = new DictionariesManager(getVerbaDirectory(), dictionaryDao);
		ArrayList<String> dictionaries = dictionariesManager.findNewDictionaries();
		if (!dictionaries.isEmpty()) {
			showLoadDictionariesDialog(dictionaries);
		}
	}

	private void showLoadDictionariesDialog(ArrayList<String> dictionaries) {
		Intent showDictionariesLoaderCommand = new Intent(this, DictionariesLoaderActivity.class);
		showDictionariesLoaderCommand.putStringArrayListExtra(NEW_DICTIONARIES, dictionaries);
		startActivity(showDictionariesLoaderCommand);
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