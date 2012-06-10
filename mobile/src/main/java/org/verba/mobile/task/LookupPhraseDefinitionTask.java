package org.verba.mobile.task;

import java.io.IOException;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.PhraseDefinitionLookup;
import org.verba.stardict.PhraseDefinition;
import org.verba.stardict.PhraseDefinitionCoordinates;

import android.os.AsyncTask;

public class LookupPhraseDefinitionTask extends AsyncTask<PhraseDefinitionCoordinates, Void, PhraseDefinition> {
	private PhraseDefinitionDetailsActivity phraseDefinitionDetailsActivity;

	public LookupPhraseDefinitionTask(PhraseDefinitionDetailsActivity phraseDefinitionDetailsActivity) {
		this.phraseDefinitionDetailsActivity = phraseDefinitionDetailsActivity;
	}

	@Override
	protected PhraseDefinition doInBackground(PhraseDefinitionCoordinates... phraseDefinitionCoordinates) {
		try {
			return new PhraseDefinitionLookup().lookupPhraseDefinition(phraseDefinitionCoordinates[0]);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Unexpected error while looking up [%s]",
					phraseDefinitionCoordinates[0].getTargetPhrase()), e);
		}
	}

	@Override
	protected void onPostExecute(PhraseDefinition phraseDefinitionFound) {
		phraseDefinitionDetailsActivity.displayPhraseDefinition(phraseDefinitionFound);
	}
}
