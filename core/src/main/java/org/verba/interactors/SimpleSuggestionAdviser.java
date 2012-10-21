package org.verba.interactors;

import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;
import org.verba.boundary.SuggestionAdviser;

public class SimpleSuggestionAdviser implements SuggestionAdviser {
	private DictionaryEntryRepository dictionaryEntryRepository;

	public SimpleSuggestionAdviser(DictionaryEntryRepository dictionaryEntryRepository) {
		this.dictionaryEntryRepository = dictionaryEntryRepository;
	}

	@Override
	public List<String> getTopSuggestions(String phrasePattern, int limit) {
		List<DictionaryEntryDataObject> suggestedDictionaryEntries =
				dictionaryEntryRepository.getTopSuggestions(phrasePattern, limit);

		return convertToSuggestedPhrases(suggestedDictionaryEntries);
	}

	// TODO: Rewrite using LambdaJ ?
	private List<String> convertToSuggestedPhrases(List<DictionaryEntryDataObject> suggestedDictionaryEntries) {
		List<String> suggestedPhrases = new ArrayList<String>();
		for (DictionaryEntryDataObject dictionaryEntry : suggestedDictionaryEntries) {
			suggestedPhrases.add(dictionaryEntry.getPhrase());
		}
		return suggestedPhrases;
	}

}
