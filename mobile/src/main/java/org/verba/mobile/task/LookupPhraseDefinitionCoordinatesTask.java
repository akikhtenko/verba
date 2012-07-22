package org.verba.mobile.task;

import java.util.List;

import org.verba.mobile.PhraseDefinitionDetailsActivity;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.stardict.DictionaryEntryDao.NoDictionaryEntryFoundException;
import org.verba.mobile.stardict.DictionaryEntryDataObject;

import android.os.AsyncTask;

public class LookupPhraseDefinitionCoordinatesTask extends AsyncTask<String, Void, List<DictionaryEntryDataObject>> {
	private PhraseDefinitionDetailsActivity activity;
	private DictionaryEntryDao dictionaryEntryDao;

	public LookupPhraseDefinitionCoordinatesTask(PhraseDefinitionDetailsActivity activity,
			DictionaryEntryDao dictionaryEntryDao) {
		this.activity = activity;
		this.dictionaryEntryDao = dictionaryEntryDao;
	}

	@Override
	protected List<DictionaryEntryDataObject> doInBackground(String... wordsToLookup) {
		try {
			return dictionaryEntryDao.getDictionaryEntriesByPhrase(wordsToLookup[0]);
		} catch (NoDictionaryEntryFoundException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<DictionaryEntryDataObject> dictionaryEntriesFound) {
		if (dictionaryEntriesFound == null) {
			activity.displayPhraseDefinitionCoordinatesNotFound();
		} else {
			activity.lookupPhraseDefinition(dictionaryEntriesFound);
		}
	}
}