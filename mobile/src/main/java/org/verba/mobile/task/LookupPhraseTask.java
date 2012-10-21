package org.verba.mobile.task;

import java.util.List;

import org.verba.interactors.LookupPhrase;
import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.stardict.PhraseDefinition;

import roboguice.util.RoboAsyncTask;

public class LookupPhraseTask extends RoboAsyncTask<List<PhraseDefinition>> {
	private PhraseDefinitionDetailsActivity activity;
	private LookupPhrase lookupPhrase;
	private String phrase;

	public LookupPhraseTask(PhraseDefinitionDetailsActivity activity, LookupPhrase lookupPhrase, String phrase) {
		super(activity);
		this.activity = activity;
		this.lookupPhrase = lookupPhrase;
		this.phrase = phrase;
	}

	@Override
	public List<PhraseDefinition> call() throws Exception {
		return lookupPhrase.with(phrase);
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