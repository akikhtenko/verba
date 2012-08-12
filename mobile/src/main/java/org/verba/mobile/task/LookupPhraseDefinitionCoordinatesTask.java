package org.verba.mobile.task;

import java.util.List;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.stardict.DictionaryEntryDataObject;

import roboguice.util.RoboAsyncTask;

public class LookupPhraseDefinitionCoordinatesTask extends RoboAsyncTask<List<DictionaryEntryDataObject>> {
	private PhraseDefinitionDetailsActivity activity;
	private DictionaryEntryDao dictionaryEntryDao;
	private String phraseToLookup;

	public LookupPhraseDefinitionCoordinatesTask(PhraseDefinitionDetailsActivity activity,
			DictionaryEntryDao dictionaryEntryDao, String wordToLookup) {
		super(activity);
		this.activity = activity;
		this.dictionaryEntryDao = dictionaryEntryDao;
		this.phraseToLookup = wordToLookup;
	}

	@Override
	public List<DictionaryEntryDataObject> call() throws Exception {
		return dictionaryEntryDao.getDictionaryEntriesByPhrase(phraseToLookup);
	}

	@Override
	protected void onSuccess(List<DictionaryEntryDataObject> dictionaryEntriesFound) throws Exception {
		activity.lookupPhraseDefinition(dictionaryEntriesFound);
	}

	@Override
	protected void onException(Exception e) {
		activity.displayPhraseDefinitionCoordinatesNotFound();
	}
}