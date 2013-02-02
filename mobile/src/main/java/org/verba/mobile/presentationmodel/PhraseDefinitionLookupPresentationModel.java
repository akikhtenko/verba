package org.verba.mobile.presentationmodel;

import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.verba.mobile.PhraseDefinitionDetailsActivity.PHRASE_TO_LOOKUP;

import java.util.List;

import org.robobinding.presentationmodel.DependsOnStateOf;
import org.robobinding.presentationmodel.ItemPresentationModel;
import org.robobinding.presentationmodel.PresentationModel;
import org.robobinding.viewattribute.adapterview.ItemClickEvent;
import org.verba.interactors.GetSuggestions;
import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.R;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.widget.ListView;

import com.google.inject.Inject;

@PresentationModel
public class PhraseDefinitionLookupPresentationModel {
	private static final int SUGGESTIONS_LIMIT = 100;
	@Inject private GetSuggestions getSuggestions;
	@InjectView(R.id.phraseSuggestions) private ListView phraseSuggestionsList;
	private Activity activity;
	private String phraseToLookup = EMPTY;

	@Inject
	public PhraseDefinitionLookupPresentationModel(Activity activity) {
		this.activity = activity;
	}

	public String getPhraseToLookup() {
		return phraseToLookup;
	}

	public void setPhraseToLookup(String phraseToLookup) {
		this.phraseToLookup = phraseToLookup;
	}

	public void lookupPhrase() {
		lookupPhrase(phraseToLookup);
	}

	public void phraseSuggestionSelected(ItemClickEvent event) {
		String selectedSuggestion = (String) phraseSuggestionsList.getItemAtPosition(event.getPosition());
		setPhraseToLookup(selectedSuggestion);

		lookupPhrase(selectedSuggestion);
	}

	@DependsOnStateOf("phraseToLookup")
	@ItemPresentationModel(PhraseSuggestionItemPresentationModel.class)
	public List<String> getSuggestions() {
		return getSuggestions.top(SUGGESTIONS_LIMIT).with(phraseToLookup);
	}

	private void lookupPhrase(String phrase) {
		Intent commandToOpenPhraseDefinitionDetails = new Intent(activity, PhraseDefinitionDetailsActivity.class);
		commandToOpenPhraseDefinitionDetails.putExtra(PHRASE_TO_LOOKUP, phrase);
		activity.startActivity(commandToOpenPhraseDefinitionDetails);
	}
}
