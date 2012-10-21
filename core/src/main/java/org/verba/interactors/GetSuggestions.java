package org.verba.interactors;

import static java.lang.Integer.MAX_VALUE;

import java.util.ArrayList;
import java.util.List;

import org.verba.DictionaryEntryDataObject;
import org.verba.DictionaryEntryRepository;

public class GetSuggestions {
	private DictionaryEntryRepository dictionaryEntryRepository;
	private int limit = MAX_VALUE;

	public GetSuggestions(DictionaryEntryRepository dictionaryEntryRepository) {
		this.dictionaryEntryRepository = dictionaryEntryRepository;
	}

	public GetSuggestions top(int newLimit) {
		limit = newLimit;
		return this;
	}

	public List<String> with(String phrasePattern) {
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
