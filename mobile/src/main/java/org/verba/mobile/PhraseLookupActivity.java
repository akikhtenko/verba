package org.verba.mobile;

import static org.verba.mobile.PhraseDefinitionDetailsActivity.PHRASE_TO_LOOKUP;

import java.util.ArrayList;
import java.util.List;

import org.verba.boundary.NewDictionariesScanner;
import org.verba.boundary.SuggestionAdviser;

import roboguice.inject.InjectView;
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

import com.google.inject.Inject;

public class PhraseLookupActivity extends VerbaActivity implements OnClickListener, TextWatcher,
		OnItemClickListener {
	public static final String NEW_DICTIONARIES = "newDictionaries";
	private static final int SUGGESTIONS_LIMIT = 100;
	@InjectView(R.id.wordToFindField) private EditText phraseToLookupField;
	@InjectView(R.id.phraseSuggestions) private ListView phraseSuggestionsList;
	@Inject private NewDictionariesScanner newDictionariesScanner;
	@Inject private SuggestionAdviser suggestionAdviser;

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
		phraseSuggestionsList.setOnItemClickListener(this);
	}

	@Override
	protected boolean loadSystemMenu() {
		return true;
	}

	@Override
	protected int getContentLayout() {
		return R.layout.phrase_definition_lookup;
	}

	private void setupLookupButton() {
		Button button = (Button) findViewById(R.id.lookupButton);
		button.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupRestoreSafePhraseChangeListener();
		checkForNewDictionaries();
	}

	private void setupRestoreSafePhraseChangeListener() {
		phraseToLookupField.addTextChangedListener(this);
	}

	private void checkForNewDictionaries() {
		ArrayList<String> newDictionaries = newDictionariesScanner.findNewDictionaries();
		if (!newDictionaries.isEmpty()) {
			showLoadDictionariesDialog(newDictionaries);
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
			displaySuggestions(text);
		}
	}

	private void displaySuggestions(Editable text) {
		List<String> suggestions = suggestionAdviser.getTopSuggestions(text.toString(), SUGGESTIONS_LIMIT);

		ListAdapter suggestionsListDatasource =
				new ArrayAdapter<String>(this, R.layout.list_item, R.id.listItemTitle, suggestions);
		phraseSuggestionsList.setAdapter(suggestionsListDatasource);
	}

	@Override
	public void beforeTextChanged(CharSequence text, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String selectedSuggestion = (String) phraseSuggestionsList.getItemAtPosition(position);

		lookupPhrase(selectedSuggestion);
	}
}