package org.verba.mobile.task;

import org.verba.mobile.DictionaryActivity;
import org.verba.mobile.stardict.DictionaryEntryDao;
import org.verba.mobile.stardict.DictionaryEntryDao.NoDictionaryEntryFoundException;
import org.verba.mobile.stardict.DictionaryEntryDataObject;
import org.verba.stardict.PhraseDefinitionCoordinates;

import android.os.AsyncTask;

public class LookupPhraseDefinitionCoordinatesTask extends AsyncTask<String, Void, PhraseDefinitionCoordinates> {
	private DictionaryActivity dictionaryActivity;
	private DictionaryEntryDao dictionaryEntryDao;

	public LookupPhraseDefinitionCoordinatesTask(DictionaryActivity phraseLookupActivity,
			DictionaryEntryDao dictionaryEntryDao) {
		this.dictionaryActivity = phraseLookupActivity;
		this.dictionaryEntryDao = dictionaryEntryDao;
	}

	@Override
	protected PhraseDefinitionCoordinates doInBackground(String... wordsToLookup) {
		try {
			DictionaryEntryDataObject dictionaryEntry = dictionaryEntryDao
					.getDictionaryEntryByPhrasePattern(wordsToLookup[0]);

			return dictionaryEntry.asPhraseDefinitionCoordinates();
		} catch (NoDictionaryEntryFoundException e) {
			return null;
		}
	}

	@Override
	protected void onPostExecute(PhraseDefinitionCoordinates phraseDefinitionCoordinatesFound) {
		if (phraseDefinitionCoordinatesFound == null) {
			dictionaryActivity.displayPhraseDefinitionNotFound();
		} else {
			dictionaryActivity.displayPhraseDefinition(phraseDefinitionCoordinatesFound);
		}
	}
}