package org.verba;

public interface DictionaryEntryRepository {
	void doInOneGo(Operation operation) throws Exception;
	void addDictionaryEntry(DictionaryEntryDataObject dictionaryEntry);

	public interface Operation {
		void doInOneGo() throws Exception;
	}
}
