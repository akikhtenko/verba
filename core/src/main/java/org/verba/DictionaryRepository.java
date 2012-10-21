package org.verba;

public interface DictionaryRepository {
	int addDictionary(DictionaryDataObject dictionaryDataObject);
	boolean exists(String dictionaryName);
	DictionaryDataObject getDictionaryById(int dictionaryId);
}
