package org.verba.mobile.task;

import java.util.List;

import org.verba.boundary.PhraseLookup;
import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.stardict.PhraseDefinition;

import roboguice.util.RoboAsyncTask;

public class LookupPhraseTask extends RoboAsyncTask<List<PhraseDefinition>> {
	private PhraseDefinitionDetailsActivity activity;
	private PhraseLookup phraseLookup;
	private String phrase;

	public LookupPhraseTask(PhraseDefinitionDetailsActivity activity, PhraseLookup phraseLookup, String phrase) {
		super(activity);
		this.activity = activity;
		this.phraseLookup = phraseLookup;
		this.phrase = phrase;
	}

	@Override
	public List<PhraseDefinition> call() throws Exception {
		return phraseLookup.lookup(phrase);
	}

	@Override
	protected void onSuccess(List<PhraseDefinition> phraseDefinitions) throws Exception {
		for (PhraseDefinition phraseDefinition : phraseDefinitions) {
			activity.displayPhraseDefinition(phraseDefinition);
		}
	}

	@Override
	protected void onException(Exception e) {
		activity.displayPhraseLookupFailure();
	}
}