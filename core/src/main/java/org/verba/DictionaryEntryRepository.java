package org.verba;

import java.util.List;

public interface DictionaryEntryRepository {
	void doInOneGo(Operation operation) throws Exception;
	void addDictionaryEntry(DictionaryEntryDataObject dictionaryEntry);
	List<DictionaryEntryDataObject> getTopSuggestions(String phrasePattern, int limit);
	List<DictionaryEntryDataObject> getEntriesByPhrase(String phrase);

	public interface Operation {
		void doInOneGo() throws Exception;
	}


}
